@echo off
cls
call "../src/clear_cache"
xcopy /s /y /q "..\tests\" "C:\Cache\" > nul
xcopy /s /y /q ".\" "C:\Cache\prolog\" > nul
pushd .
cd "C:\Cache\prolog\"
cmd /C TestProlog.cmd prtest.tree.PrologTreeTest hard SubMap
popd
