Get all meals:
curl http://localhost:8080/topjava/rest/meals

Get meal by id:
curl http://localhost:8080/topjava/rest/meals/100001

Delete meal by id:
curl -X DELETE http://localhost:8080/topjava/rest/meals/100007

Create new meal:
curl -H "Content-Type: application/json" -X POST -d '{\"dateTime\":\"2019-11-24T20:28:00\", \"description\":\"Dinner\", \"calories\":\"510\"}' http://localhost:8080/topjava/rest/meals

Update meal:
curl -H "Content-Type: application/json" -X PUT -d '{\"dateTime\":\"2019-11-24T20:15:00\", \"description\":\"Dinner\", \"calories\":\"510\"}' http://localhost:8080/topjava/rest/meals/100005

Get meals filtered by dateTime
curl http://localhost:8080/topjava/rest/meals/filter?startDate=2019-11-06&endDate=&startTime=23:00:00&endTime=
