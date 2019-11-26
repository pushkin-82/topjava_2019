### For authorized user:

#### Get all meals
curl http://localhost:8080/topjava/rest/meals

#### Get meal
##### @param id is Integer meal's id
curl http://localhost:8080/topjava/rest/meals/id

#### Delete meal
##### @param id is Integer meal's id
curl -X DELETE http://localhost:8080/topjava/rest/meals/id

#### Create new meal
##### @param dateTime_value is LocalDateTime value of meal's creation time; default value is now()
##### @param description_value is String, default value is ""
##### @param calories_value is Integer, default value is 1000
curl -H "Content-Type: application/json" -X POST -d {"dateTime":"dateTime_value", "description":"description_value", "calories":"calories_value"} http://localhost:8080/topjava/rest/meals

#### Update meal
##### @param id is Integer meal's id
##### @param dateTime_value is LocalDateTime value of meal's creation time
##### @param description_value is String
##### @param calories_value is Integer
curl -H "Content-Type: application/json" -X PUT -d {"dateTime":"dateTime_value", "description":"description_value", "calories":"calories_value"} http://localhost:8080/topjava/rest/meals/id

#### Get meals filtered by date or time
##### @param startDate_value is LocalDate value of date you want filter meals list from, may be null or empty
##### @param endDate_value is LocalDate value of date you want filter meals list to, may be null or empty
##### @param startTime_value is LocalTime value of time you want filter meals list from, may be null or empty
##### @param startTime_value is LocalTime value of time you want filter meals list to, may be null or empty
curl http://localhost:8080/topjava/rest/meals/filter?startDate=startDate_value&endDate=endDate_value&startTime=startTime_value&endTime=endTime_value
