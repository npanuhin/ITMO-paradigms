@echo off
cls
call "../src/clear_cache"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "queue/ArrayQueue.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "queue/LinkedQueue.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "../tests/java/queue/QueueTest.java"
java -cp "C:\Cache" -ea queue.QueueTest IfWhile
