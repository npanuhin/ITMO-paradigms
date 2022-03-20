@echo off
cls
call "../src/clear_cache"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "../HW3/queue/ArrayQueueModule.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "../HW3/queue/ArrayQueueADT.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "queue/ArrayQueue.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "../tests/java/queue/ArrayQueueTest.java"
java -cp "C:\Cache" -ea --add-opens java.base/java.util=ALL-UNNAMED queue.ArrayQueueTest DequeIndex
