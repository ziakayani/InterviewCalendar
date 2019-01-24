
**Interview Calendar API**
================================================

To run this application:

* Install SBT from http://www.scala-sbt.org/
* Start SBT using `sbt` (Mac and Linux) or `sbt.bat` (Windows)
* In SBT shell, `container:start`

**Add Interviewer or Candidate**
* **URL** `http://localhost:8080/v1/api/add`
* **Method:**  `PUT`
* **URL Params**  `NONE`
* **Data Params**
```json
{
  "email": "zia.kayani@platalytics",
  "personType": "Candidate",
  "availability": [
    {
      "from": "2018-01-22 08:00",
      "to": "2018-01-22 12:00"
    }
  ]
}
```

* **Success Response:**
  **Code:** `200`
  **Content:** `Interviewer added successfully`
 
* **Error Response:**
    **Content:** `ERROR: Invalid JSON.`
  OR
    **Content:** `ERROR: JSON not according to desired format.`

* **Sample Call:**
  ```shell 
    curl -X PUT \
    http://localhost:8080/v1/api/add \
        -H 'Content-Type: application/json' \
        -d '{"email":"zia.kayani@platalytics","personType":"Candidate","availability":[{"from":"2018-01-22 08:00","to":"2018-01-22 12:00"}]}'
  ```
  
 **Get Candidate**
* **URL**  `http://localhost:8080/v1/api/getCandidate`
* **Method:** `GET`
* **URL Params** `email:String`
* **Data Params** `NONE`
* **Success Response:**
  **Code:** `200`
  **Content:**
```json
{
  "email": "zia.kayani@platalytics",
  "personType": "Candidate",
  "availability": [
    {
      "from": "2018-01-22 08:00",
      "to": "2018-01-22 12:00"
    }
  ]
}
```
 
* **Error Response:**
    **Content:** `ERROR: Candidate is not found with Email zia_kayani@platalytics.com`

* **Sample Call:**
  ```shell 
    curl -X GET \
    http://localhost:8080/v1/api/getCandidate?email=zia_kayani@platalytics.com
  ```
  
**Get Interviewer**
* **URL**  `http://localhost:8080/v1/api/getInterviewer`
* **Method:** `GET`
*  **URL Params** `email:String`
* **Data Params** `NONE`
* **Success Response:**
  **Code:** `200`
  **Content:**
```json
{
  "email": "zia.kayani@platalytics",
  "personType": "Interviewer",
  "availability": [
    {
      "from": "2018-01-22 08:00",
      "to": "2018-01-22 12:00"
    }
  ]
}
```
 
* **Error Response:**
    **Content:** `ERROR: Interviewer is not found with Email zia_kayani@platalytics.com`

* **Sample Call:**
  ```shell 
    curl -X GET \
    http://localhost:8080/v1/api/getInterviewer?email=zia_kayani@platalytics.com
  ```
  
    
**Get All Time Intervals**
* **URL**  `http://localhost:8080/v1/api/getPossibleInterViews`
* **Method:** `GET`
*  **URL Params** `email:String, personType: String`
* **Data Params** `NONE`
* **Success Response:**
  **Code:** `200`
  **Content:**
```json
{
    "email": "11",
    "personType": "Candidate",
    "overlapped": [
        {
            "email": "2",
            "personType": "Interviewer",
            "availability": [
                {
                    "from": "2019-01-22 04:00",
                    "to": "2019-01-22 05:00"
                }
            ]
        },
        {
            "email": "1",
            "personType": "Interviewer",
            "availability": [
                {
                    "from": "2019-01-22 01:00",
                    "to": "2019-01-22 02:00"
                },
                {
                    "from": "2019-01-22 03:00",
                    "to": "2019-01-22 04:00"
                }
            ]
        },
        {
            "email": "3",
            "personType": "Interviewer",
            "availability": [
                {
                    "from": "2019-01-22 08:00",
                    "to": "2019-01-22 12:00"
                }
            ]
        }
    ]
}
```
 
* **Error Response:**
    **Content:** `ERROR: Interviewer is not found with Email zia_kayani@platalytics.com`
  OR
    **Content:** 
```json
{
    "email": "11",
    "personType": "Candidate",
    "overlapped": []
}
```

* **Sample Call:**
  ```shell 
    curl -X GET \
    'http://localhost:8080/v1/api/getPossibleInterViews?email=11&personType=Candidate' 
  ```
  
  