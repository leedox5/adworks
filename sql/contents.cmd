sqlcmd -S localhost\SQLEXPRESS -d TW_SYS -Q "Exec sp_CreateDataLoadScriptDB2 'TW_SYS','%1'" > Contents-%1.sql
