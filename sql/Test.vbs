On Error Resume Next
Const connstr = "Provider=SQLOLEDB.1;User ID=sa;Password=flehrtm;Initial Catalog=TW_SYS;Data Source=.\SQLEXPRESS"

Set  stdOut = WScript.StdOut

Dim objFSO, objFile
Dim strDir, strFile, strPath
strDir = "D:\project\adworks\sql\"
strFile = "SPRAQ1SA01.TXT.sql"
strPath = strDir & strFile

Set objFSO = CreateObject("Scripting.FileSystemObject")
Set objFile = objFSO.GetFile(strPath)
Set objReadFile = objFSO.OpenTextFile(strPath, 1)
strConts = objReadFile.ReadAll

objReadFile.Close

strCmd = " EXEC ( '" + Replace(strConts,"'","''") + "') AT MCAMSDEV "

Set cmd   = CreateObject("ADODB.Command")

cmd.ActiveConnection = connstr
cmd.Prepared         = True 
cmd.CommandText      = strCmd

cmd.Execute

If Err.Number <> 0 Then 
	stdOut.WriteLine "Error: " & Err.Description
End If 

Set cmd.ActiveConnection = Nothing 
Set cmd = Nothing 


