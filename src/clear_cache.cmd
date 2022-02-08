del /f /q /s "C:\Cache\*" >nul
for /d %%x in ("C:\Cache\*") do @rd /s /q "%%x" >nul
