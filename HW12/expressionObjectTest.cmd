@echo off
cls
call "../src/clear_cache"
xcopy /s /y /q "..\tests\" "C:\Cache\" > nul
xcopy /s /y /q ".\" "C:\Cache\clojure\" > nul
pushd .
cd "C:\Cache\clojure\"
cmd /C TestClojure.cmd cljtest.object.ObjectTest hard SumexpSoftmax
popd
