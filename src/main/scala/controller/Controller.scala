package controller

import com.fasterxml.jackson.core.JsonParseException

import model.IOMananger
import model.InMemoryIOManager
import model.PersonDTO
import model.PersonType
import model.ResponseDTO
import play.api.libs.json.JsResultException
import play.api.libs.json.Json

/**
 * Controller
 * Handles all the request comes from the Router
 * Using IOManager, can be create the instance of any other implementation of IOManager
 */
object Controller {

  /**
   * IOManager, used to safe the interviewerS and candidates
   */
  val ioMananger: IOMananger = new InMemoryIOManager();
  /**
   * Add person it can be Interviewer or Candidate
   *
   *  @param jsonString of Person
   *  @return String telling about the message of adding or exception
   */
  def addPerson(jsonString: String): String = {
    try {
      val person = Json.parse(jsonString).as[PersonDTO](PersonDTO.personDTOReads)
      person.personType match {
        case PersonType.Interviewer =>
          ioMananger.addInterviewer(person)
          "Interviewer added successfully"
        case PersonType.Candidate =>
          ioMananger.addCandidate(person)
          "Candidate added successfully"
      }
    } catch {
      case ex: JsResultException =>
        ex.printStackTrace()
        "ERROR: JSON not according to desired format."
      case ex: JsonParseException =>
        ex.printStackTrace()
        "ERROR: Invalid JSON."
      case ex: Exception =>
        ex.printStackTrace()
        "ERROR: Invalid Input."
    }
  }
  /**
   * Get the already added person
   *
   *  @param email
   *  @return JsonString of that particular person or null
   */
  def getPerson(email: String, personType: PersonType.PersonType): String = {
    val person = personType match {
      case PersonType.Interviewer =>
        ioMananger.getInterviewer(email)
      case PersonType.Candidate =>
        ioMananger.getCandidate(email)
    }
    if (person != null)
      // converting to string
      Json.stringify(Json.toJson(person)(PersonDTO.personDTOWrites))
    else
      s"ERROR: ${personType} is not found with Email '${email}'"
  }

  /**
   * Get all the possible interview intervals for a Person
   *
   *  @param email
   *  @param personType : either Interviewer or Candidate
   *  @return ResponceDTO string
   */

  def getInterviewAvailability(email: String, personType: String): String = {
    val response = ioMananger.getInterviewAvailability(email, PersonType.withName(personType))
    if (response != null)
      Json.stringify(Json.toJson(response)(ResponseDTO.ResponseDTOWrites));
    else
      s"ERROR: ${personType} is not found with Email '${email}'"
  }
}