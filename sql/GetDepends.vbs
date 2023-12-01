'------------------------------------------------------------------------------------------
'-- NAME
'--    GetDepends.vbs
'-- DESCRIPTION
'--    테이블에 종속된 View, Proc, Function등 가져오기
'-- NOTE
'--    
'-- MODIFIED
'--    [2011-01-17] created by Myungho Lee 
'--
'------------------------------------------------------------------------------------------
'On Error Resume Next
'Const connStr    = "Provider=SQLOLEDB.1;User ID=sa;Password=sjgni01;Initial Catalog=TW_SYS;Data Source=SJ"
Const connStr104  = "Provider=SQLOLEDB.1;Data Source=160.2.254.104;Initial Catalog=KREMAP3;User ID=ginno;Password=ginno"

'---- ExecuteOptionEnum Values ----
Const adAsyncExecute = &H00000010
Const adAsyncFetch = &H00000020
Const adAsyncFetchNonBlocking = &H00000040
Const adExecuteNoRecords = &H00000080
Const adExecuteStream = &H00000400

'---- CommandTypeEnum Values ----
Const adCmdUnknown     = &H0008
Const adCmdText        = &H0001
Const adCmdTable       = &H0002
Const adCmdStoredProc  = &H0004
Const adCmdFile        = &H0100
Const adCmdTableDirect = &H0200

'---- ParameterDirectionEnum Values ----
Const adParamUnknown = &H0000
Const adParamInput = &H0001
Const adParamOutput = &H0002
Const adParamInputOutput = &H0003
Const adParamReturnValue = &H0004

'---- DataTypeEnum Values ----
Const adEmpty = 0
Const adTinyInt = 16
Const adSmallInt = 2
Const adInteger = 3
Const adBigInt = 20
Const adUnsignedTinyInt = 17
Const adUnsignedSmallInt = 18
Const adUnsignedInt = 19
Const adUnsignedBigInt = 21
Const adSingle = 4
Const adDouble = 5
Const adCurrency = 6
Const adDecimal = 14
Const adNumeric = 131
Const adBoolean = 11
Const adError = 10
Const adUserDefined = 132
Const adVariant = 12
Const adIDispatch = 9
Const adIUnknown = 13
Const adGUID = 72
Const adDate = 7
Const adDBDate = 133
Const adDBTime = 134
Const adDBTimeStamp = 135
Const adBSTR = 8
Const adChar = 129
Const adVarChar = 200
Const adLongVarChar = 201
Const adWChar = 130
Const adVarWChar = 202
Const adLongVarWChar = 203
Const adBinary = 128
Const adVarBinary = 204
Const adLongVarBinary = 205
Const adChapter = 136
Const adFileTime = 64
Const adPropVariant = 138
Const adVarNumeric = 139
Const adArray = &H2000

Set  stdOut = WScript.StdOut
Dim OTYPE

OTYPE = GetArgs( "t", "NONE"  )

Main

Sub Main()
	Dim tbls, tbl
	
	tbls = Array(                                  _
		"AD_REPORT_FILE"                           _
	  ,	"AD_REPORT"                                _
	  ,	"AD_CODE"                                  _
	  ,	"AD_CODE_GROUP"                            _
	  , "AD_LAWCODE"                               _
	  ,	"GS_MODEL_VAR"                             _
	  ,	"GS_ANS_MODEL"                             _
	  ,	"GS_ANS_RESULT"                            _
	  ,	"GS_AREA_GROUP"                            _
	  ,	"GS_GROUP_LAW"                             _
	  ,	"GS_AREA_LEGEND"                           _
	  ,	"MS_R_IMG"                                 _
	  ,	"MS_R_WORK"                                _
	  ,	"MS_T_WORK"                                _
	  ,	"MS_R_RESULT"                              _
	  ,	"MS_APT_INFO"                              _
	  ,	"MS_LAND_LIST"                             _
	  ,	"MS_LAND_DESC"                             _
	  ,	"MS_LAND_INFO"                             _
	  ,	"MS_ANS_AREA"                              _
	  ,	"MS_R_GROUP"                               _
	  ,	"MS_T_GROUP"                               _
	  ,	"MS_THEME"                                 _
	  ,	"MS_APT_LIST"                              _
	  ,	"MS_ANS_MODEL"                             _
	  ,	"PI_ANS_RESULT"                            _
	  ,	"PI_ANS_MODEL"                             _
	  ,	"PI_MODEL_VAR"                             _
	  ,	"PI_R_WORK"                                _
	  ,	"PI_AREA_LEGEND"                           _
	  ,	"PO_POLICY"                                _
	  ,	"PO_POLICY_RATE"                           _
	  ,	"SI_ANS_RESULT"                            _
	  ,	"SI_ANS_MODEL"                             _
	  ,	"SI_MODEL_VAR"                             _
	  ,	"SI_AREA_LEGEND"                           _
	  ,	"ST_STATUS_SD"                             _
	  ,	"ST_STATUS_SGG"                            _
	  ,	"ST_STATUS_GROUP"                          _
	  ,	"ST_STATUS"                                _
	  ,	"SE_USE_LCH_EVIEWS"                        _
	  ,	"SE_USE_LCH"                               _
	  ,	"SE_TRA_SIZE"                              _
	  ,	"SE_ARCHAREA_CHAK"                         _
	  ,	"SE_APTUSE_TRADE"                          _
	  ,	"SE_JEN_ROW"                               _
	  ,	"SE_LIQUID"                                _
	  ,	"SE_TYPE_RENT"                             _
	  ,	"SE_TRA_TR"                                _
	  ,	"SE_COMPANY3Y_BOND"                        _
	  ,	"SE_MBY_SGGK"                              _
	  ,	"SE_MBY_SD"                                _
	  ,	"SE_JEN_DEMSUP"                            _
	  ,	"SE_ARCHNUM_HEGA"                          _
	  ,	"SE_JEN_RTE"                               _
	  ,	"SE_TRA_DET"                               _
	  ,	"SE_TRA_APT"                               _
	  ,	"SE_TRA_ROW"                               _
	  ,	"SE_REM_INTRATE"                           _
	  ,	"SE_MBY_SDSGGK"                            _
	  ,	"SE_GOVERNMENT3Y_BOND"                     _
	  ,	"SE_KOSPI"                                 _
	  ,	"SE_ITEM_LCH"                              _
	  ,	"SE_MONET"                                 _
	  ,	"SE_MON_RATE"                              _
	  ,	"SE_RATE"                                  _
	  ,	"SE_NEW_INTRATE"                           _
	  ,	"SE_GENERNAL1_INDEX"                       _
	  ,	"SE_APTSIZE_TRADE"                         _
	  ,	"SE_JEN_APT"                               _
	  ,	"SE_MBY_AMA"                               _
	  ,	"SE_MBY_AKA"                               _
	  ,	"SE_MBY_SGGAMA"                            _
	  ,	"SE_ARCHAREA_CHAK_SEASON"                  _
	  ,	"SE_LAND_LTR_SEASON"                       _
	  ,	"SE_DEPOSIT"                               _
	  ,	"SE_BSI_01"                                _
	  ,	"SE_BSI_02"                                _
	  ,	"SE_APT_RENT_PRC"                          _
	  ,	"SE_SEL_BUY"                               _
	  ,	"SE_JEN_TRAN"                              _
	  ,	"SE_ARCHAREA_HEGA_SEASON"                  _
	  ,	"SE_JEN_DET"                               _
	  ,	"SE_ARCHNUM_CHAK"                          _
	  ,	"SE_JEN_TOTAL"                             _
	  ,	"SE_LAND_LTR"                              _
	  ,	"SE_TRA_TOTAL"                             _
	  ,	"SE_COMPOSITE_INDEX"                       _
	  ,	"SE_GENERNAL2_INDEX"                       _
	  ,	"SE_GENERNAL3_INDEX"                       _
	  ,	"SE_GENERNAL4_INDEX"                       _
	  ,	"SE_LIQUID_SEASON"                         _
	  ,	"SE_ARCHAREA_HEGA"                         _
	  ,	"SE_MBY_MA"                                _
	  ,	"SE_CD_BOND"                               _
	  ,	"SE_JEN_SIZE"                              _
	  ,	"SE_MBY_KA"                                _
	  ,	"SE_BTYPE_TRADE"                           _
	  ,	"SE_ITEM_LTR"                              _
	  ,	"MI_KRE_SVYINFO"                           _
	  ,	"MI_KRE_CON"                               _
	  ,	"MI_KRE_QUESTION"                          _
	  ,	"MI_KRE_REPLYCON"                          _
	  ,	"MI_KRE_RGRP"                              _
	  ,	"MI_KRE_CGRP"                              _
	  ,	"MI_KRE_QGRP"                              _
	  ,	"MI_KRE_PNLINFO"                           _
	  ,	"MI_KRE_REPLY"                             _
	  ,	"MI_KRE_REPLYER"                           _
	  ,	"MI_IDX_NEW"                               _
	  ,	"MI_REP_MANAGE"                            _
	  ,	"MI_GAJOONG"                               _
	  ,	"MI_SVY_MANAGE"                            _
	  ,	"MI_IDX_SETTING"                           _
	  ,	"MI_IDX_SETTING_NEW"                       _
	  ,	"MI_IDX"                                   _
	  ,	"MI_IDX_FUR"                               _
	  ,	"MI_SVYQST_GROUP"                          _
	  ,	"MI_REPLY_GROUP"                           _
	  ,	"MI_QSTREP_GROUP"                          _
	  ,	"MI_AGN_RESULT"                            _
	  ,	"MI_PUB_RESULT"                            _
	  ,	"MI_PRO_RESULT"                            _
	  ,	"MI_QST_MANAGE"                            _
	)

	For Each tbl In tbls
		Call GetObjs(tbl)
	Next  
End Sub 

Sub GetObjs(tbl)
	Dim cmd
	Dim rst
	Dim rs

	'stdOut.WriteLine "Start: " & proc

	Set cmd = CreateObject("ADODB.Command")
	Set rs  = CreateObject("ADODB.Recordset")

	cmd.ActiveConnection = connStr104
	cmd.Prepared         = True 
	'cmd.CommandType      = adCmdStoredProc
	'cmd.CommandText      = "sp_depends"
	cmd.CommandText = "SELECT distinct xtype,  so.name            " _
					& "FROM syscomments sc                        " _ 
					& "INNER JOIN sysobjects so ON sc.id = so.id  " _
					& "WHERE charindex('" & tbl &"', text) > 0    " _
					& "ORDER BY 1,2                               "

	'cmd.Parameters("@objname").Value = tbl
	'cmd.Parameters.Append cmd.CreateParameter("@objname", adVarChar, adParamInput, 776)
	
	'cmd.Execute , , adExecuteNoRecords

	'If Err.Number <> 0 Then 
	'	stdOut.WriteLine "Error: " & Err.Description
	'End If 
	'rst = cmd.Parameters("@RST").Value
	Set rs = cmd.Execute

	Do While Not rs.EOF
		'stdOut.WriteLine rs(0).Value & "|" & OTYPE
		If rs(0).Value = OTYPE Then 
			stdOut.WriteLine   Left(tbl         & "                                  ",20) _
								  & Left(rs(0).Value & "    ",5) _ 
							 & rs(1).Value
			'For k = 0 To rs.Fields.Count -1 
			'	stdOut.WriteLine rs(k).Value
			'Next 
		End If 
		rs.MoveNext
	Loop 

	If Err.Number <> 0 Then 
		stdOut.WriteLine "Error:" & Err.Number
	End If 

	rs.Close
	'cleanup
	Set cmd.ActiveConnection = Nothing 
	Set cmd = Nothing 
	Set rs = Nothing 


	'stdOut.WriteLine rst
End Sub 


'-----------------------------------------------------
Function GetArgs( sSwitch, sDefaultValue )
'-----------------------------------------------------
' Checks the command line arguments for a given switch and returns the associated
' string, if found. If not found, the defaultValue is returned instead.
	Dim ArgCount, bMatch
	ArgCount = 0
	bMatch = 0
	Do While ArgCount < WScript.arguments.length
		If Eval((WScript.arguments.item(ArgCount)) = ("-" + (sSwitch))) Or Eval((WScript.arguments.item(ArgCount)) = ("/" + (sSwitch))) then
			bMatch = 1
			Exit Do
		Else 
			ArgCount = ArgCount + 1
		End If 
	Loop
	If ( bMatch = 1 ) Then 
		GetArgs = ( WScript.arguments.item(ArgCount + 1) )
	Else 
		GetArgs = ( sDefaultValue )
	End If 
End Function

