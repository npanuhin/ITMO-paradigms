@echo off
cls
call "../src/clear_cache"
xcopy /s /y /q "..\tests\" "C:\Cache\" > nul
copy /y "linear.clj" "C:\Cache\clojure\" > nul
pushd .
cd "C:\Cache\clojure\"
cmd /C TestClojure.cmd cljtest.linear.LinearTest hard Base
popd
