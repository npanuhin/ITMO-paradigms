@echo off
cls
call "../src/clear_cache"
xcopy /s /y /q "..\tests\" "C:\Cache\" > nul
copy /y "objectExpression.js" "C:\Cache\javascript\" > nul
pushd .
cd "C:\Cache\javascript\"
cmd /C TestJS.cmd jstest.prefix.PrefixTest hard Base && TestJS.cmd jstest.prefix.PostfixTest hard SumexpSoftmax
popd