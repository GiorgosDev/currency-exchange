# currency-exchange
Java 8 currency exchange Restful API to exchange currency based on ECB service

API:


REQUEST

To convert currency please make GET Call to service:

http://Server_URL/api/convert


parameters:

from - currency from name shortened by ISO standard, i.e. EUR

to - currency to name shortened by ISO standard, i.e. USD

amount - amount in currency from to be converted, i.e 123.21

date - optional date of operation in format yyyy-MM-dd, i.e. 2017-05-15. If date is not mentioned then today date by server time will be used. If request is done on weekend or holiday, rate will be calulated for the first bank work day prior to date requested.
If date is more than today or less then 90 days before today then corresponding error message will be provided.


request example:

http://localhost:8080/api/convert?from=USD&to=EUR&amount=100.2&date=2017-05-01

http://localhost:8080/api/convert?from=USD&to=EUR&amount=100.2


RESPONSE

Success Response

{"responseCode":200,"message":"SUCCESS","payload":110437.7}

HTTP Status OK used to indicate successful conversion,
payload used to deliver result of conversion


Error Response

{"responseCode":500,"message":"FAILED","payload":"No data for requested currency present."}

HTTP ERROR CODES 400 and 500 are used to indicate conversion error

