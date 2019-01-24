package test

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.specs2.Specification
import org.specs2.specification.Fragments

import controller.Controller
import model.Duration
import model.PersonDTO
import model.PersonType
import play.api.libs.json.Json

/**
 *
 * Testing all the possible Result of the Interview Calander
 *
 */

object TestInterviewCalender extends Specification {

  val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")

  def is: Fragments = {
    // Test in the following order
    s"""
    Testing the Json Writer ${TestJSONReader()} 
    Testing the Json Reader ${TestJSONWriter()} 
    Testing the Adding Person ${TestAddingPerson()}
    Verify the Persons Added ${VerifyAddedPerson()}
    Test List of Interviews ${TestInterviews()}
    """
  }

  def TestJSONReader() = {
    val jsonString = """{"email":"zia.kayani@platalytics.com","personType":"Interviewer","availability":[{"from":"2019-01-22 01:00","to":"2019-01-22 02:00"}, {"from":"2019-01-22 03:00","to":"2019-01-22 04:00"}]}"""
    val interviewer = Json.parse(jsonString).as[PersonDTO](PersonDTO.personDTOReads)

    interviewer.personType must beTheSameAs(PersonType.Interviewer)
    interviewer.availability.size === (2)
  }

  def TestJSONWriter() = {
    val candidate = PersonDTO("zia.kayani@hotmail.com", PersonType.Candidate,
      Seq(Duration(DateTime.parse("2019-01-22 01:00", formatter), DateTime.parse("2019-01-22 02:00", formatter)),
        Duration(DateTime.parse("2019-01-22 03:00", formatter), DateTime.parse("2019-01-22 04:00", formatter))))

    val jsonString = Json.stringify(Json.toJson(candidate)(PersonDTO.personDTOWrites))
    val jsonStringExpected = """{"email":"zia.kayani@platalytics.com","personType":"Candidate","availability":[{"from":"2019-01-22 01:00","to":"2019-01-22 02:00"}, {"from":"2019-01-22 03:00","to":"2019-01-22 04:00"}]}"""
    jsonString must_== jsonStringExpected
  }

  def TestAddingPerson() = {
    val interviewers = Seq(
      """{"email":"1","personType":"Interviewer","availability":[{"from":"2019-01-22 01:00","to":"2019-01-22 02:00"}, {"from":"2019-01-22 03:00","to":"2019-01-22 04:00"}]}""",
      """{"email":"2","personType":"Interviewer","availability":[{"from":"2019-01-22 04:00","to":"2019-01-22 05:00"}]}""",
      """{"email":"3","personType":"Interviewer","availability":[{"from":"2019-01-22 08:00","to":"2019-01-22 12:00"}]}""")

    val candidates = Seq(
      """{"email":"11","personType":"Candidate","availability":[{"from":"2019-01-22 01:00","to":"2019-01-22 02:00"}, {"from":"2019-01-22 03:00","to":"2019-01-22 05:00"}, {"from":"2019-01-22 08:00","to":"2019-01-22 12:00"}]}""",
      """{"email":"12","personType":"Candidate","availability":[{"from":"2019-01-22 04:00","to":"2019-01-22 05:00"}]}""",
      """{"email":"13","personType":"Candidate","availability":[{"from":"2018-01-22 08:00","to":"2018-01-22 12:00"}]}""")

    interviewers.foreach { interviewer =>
      Controller.addPerson(interviewer) must_== "Interviewer added successfully"
    }
    candidates.foreach { Candidate =>
      Controller.addPerson(Candidate) must_== "Candidate added successfully"
    }
  }
  def VerifyAddedPerson() = {
    Controller.getPerson("1", PersonType.Interviewer) must not be null
    Controller.getPerson("11", PersonType.Candidate) must not be null
  }

  def TestInterviews() = {
    Controller.getInterviewAvailability("11", PersonType.Candidate.toString()) === """{"email":"11","personType":"Candidate","overlapped":[{"email":"2","personType":"Interviewer","availability":[{"from":"2019-01-2204:00","to":"2019-01-2205:00"}]},{"email":"1","personType":"Interviewer","availability":[{"from":"2019-01-2201:00","to":"2019-01-2202:00"},{"from":"2019-01-2203:00","to":"2019-01-2204:00"}]},{"email":"3","personType":"Interviewer","availability":[{"from":"2019-01-2208:00","to":"2019-01-2212:00"}]}]}"""
  }
}