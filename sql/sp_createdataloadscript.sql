IF EXISTS (
	SELECT *
	FROM INFORMATION_SCHEMA.ROUTINES
	WHERE SPECIFIC_NAME = 'sp_CreateDataLoadScript'
)
	DROP PROCEDURE sp_CreateDataLoadScript
GO

CREATE Procedure [dbo].[sp_CreateDataLoadScript]
------------------------------------------------------------------------------------------
-- NAME
--    sp_CreateDataLoadScript.sql
-- DESCRIPTION
--    컨텐츠를 추가하기 위한 프로시저 생성
-- NOTE
--    sqlcmd -E -d TW_SYS -i sp_CreateDataLoadScript.sql 
--    프로시저화
-- MODIFIED
--    [2010-04-02] created by Myungho Lee
------------------------------------------------------------------------------------------
@databaseName	varchar(128) ,
@TblNames varchar(max)
as begin

	set nocount on;

	create table #a (id int identity (1,1), ColType int, ColName varchar(128))
	create table #out (lnr int, statements varchar(max))
	declare @sql nvarchar(4000)
	declare @TblName as varchar(128)
	declare @idx as bigint
	declare @previdx as bigint
	declare @last as bit
	declare @hasIdentity as bit
	declare @lnr as int

	set @lnr=0

	set @idx=charindex(',',@TblNames)
	set @previdx=1

	if @idx>0 begin  /* many tables */
		set @TblName=ltrim(rtrim(substring(@TblNames,@previdx,@idx-@previdx)))
		set @previdx=@idx+1
		set @last=0
	end
	else begin /* 1 table */
		set @TblName=ltrim(rtrim(@TblNames))
		set @last=1
	end

	while len(@TblName)>0 begin
	

		select @sql = 'select case when DATA_TYPE like ''%char%'' or DATA_TYPE = ''text'' or DATA_TYPE like ''%date%'' or DATA_TYPE like ''uniqueidentifier'' then 1 else 0 end , COLUMN_NAME
			from 	information_schema.columns
			where 	TABLE_NAME = ''' + @TblName + '''
			order by ORDINAL_POSITION
			'

		select 	@sql = 'exec ' +  @databaseName + '.dbo.sp_executesql N''' + replace(@sql, '''', '''''') + ''''
		
		--print @sql
		insert 	#a (ColType, ColName)
		exec (@sql)
		

		select @hasIdentity=max(cast(clmns.is_identity as int))
		from sys.tables AS tbl
		INNER JOIN sys.all_columns AS clmns ON  clmns.object_id = tbl.object_id
		where 	tbl.name = @TblName

		declare	@id int ,
		@maxid int ,
		@cmd1 varchar(7000) ,
		@cmd2 varchar(7000)

		insert into #out select @lnr, '/* ' + @TblName + ' */'
		set @lnr = @lnr+1
		insert into #out select @lnr, 'truncate table ' + @TblName
		set @lnr = @lnr+1

		if @hasIdentity=1 begin
			insert into #out select @lnr, 'set identity_insert ' + @TblName + ' ON'
			set @lnr = @lnr+1
		end
			
		select 	@id = 0 , @maxid = max(id)
		from 	#a

		select	@cmd1 = 'insert into #out select ' + cast(@lnr as varchar) + ', '' insert ' + @TblName + ' ( '
		select	@cmd2 = ' + '' select '' + '
		while @id < @maxid
		begin
			select @id = min(id) from #a where id > @id

			select 	@cmd1 = @cmd1 + '[' + ColName + '],'
			from	#a
			where	id = @id

			select @cmd2 = 	@cmd2
					+ ' case when [' + ColName + '] is null '
					+	' then ''null'' '
					+	' else '
					+	  case when ColType = 1 then  ''''''''' + replace(convert(varchar(max),[' + ColName + ']),'''''''','''''''''''') + ''''''''' else 'convert(varchar(50),[' + ColName + '])' end
					+ ' end + '','' + '
			from	#a
			where	id = @id
		end

		select @cmd1 = left(@cmd1,len(@cmd1)-1) + ' ) '' '
		select @cmd2 = left(@cmd2,len(@cmd2)-8) + ' from ' + @databaseName + '.dbo.' + @tblName

		exec (@cmd1 + @cmd2)
		truncate table #a

		if @hasIdentity=1 begin
			insert into #out select @lnr, 'set identity_insert ' + @TblName + ' OFF'
			set @lnr = @lnr+1
		end

		insert into #out select @lnr, 'go'
		set @lnr = @lnr+1

		-- next table
		set @idx=charindex(',',@TblNames,@previdx)
		if @idx>0 begin  /* more tables */
			set @TblName=ltrim(rtrim(substring(@TblNames,@previdx,@idx-@previdx)))
			set @previdx=@idx+1
		end
		else if @last=0 begin /* one more */
			set @TblName=ltrim(rtrim(substring(@TblNames,@previdx,8000)))
			set @last=1
		end
		else  /* done */
			set @TblName=''
	end

	drop table #a

	--print 'use ' + @databaseName
	--print 'go'
	print 'set nocount on'


	declare @statement varchar(max)
	declare @o int
	declare c_out cursor  LOCAL FAST_FORWARD for select statements from #out order by lnr
	open c_out

	declare @i int
	set @i=0
	fetch next from c_out into @statement

	while @@fetch_status=0 begin
		set @i=@i+1
		if @i=1000 begin
			print 'go'
			set @i=0
		end

		print @statement

		fetch next from c_out into @statement
	end

	close c_out
	deallocate c_out
	drop table #out
end