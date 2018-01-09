@ECHO
forfiles /s /m *.avro /c "cmd /c java -jar D:\Database\Avro\avro\avro-tools-1.7.7.jar tojson @file>@fname.json"

forfiles /s /m *.avro /c "cmd /c Del @file"