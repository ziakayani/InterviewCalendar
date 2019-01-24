package model

import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.format.DateTimeFormat

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.Format
import play.api.libs.json.JsPath
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json.Reads
import play.api.libs.json.Reads.StringReads
import play.api.libs.json.Reads.applicative
import play.api.libs.json.Reads.functorReads
import play.api.libs.json.Writes

// Defining Person Possible Types
object PersonType extends Enumeration {
  type PersonType = Value
  val Interviewer, Candidate = Value
}
case class Duration(from: DateTime, to: DateTime) {
  // store the interval
  val interal = new Interval(from, to)
}
case class PersonDTO(email: String, personType: PersonType.PersonType, availability: Seq[Duration])

object PersonDTO {

  val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")

  // JSON Reader and Writer 
  implicit val personTypeReadsWrites = new Format[PersonType.PersonType] {
    def reads(json: JsValue) = JsSuccess(PersonType.withName(json.as[String]))
    def writes(myEnum: PersonType.PersonType) = JsString(myEnum.toString)
  }

  implicit val durationWrites = new Writes[Duration] {
    def writes(duration: Duration) = Json.obj(
      "from" -> duration.from.toString(formatter),
      "to" -> duration.to.toString(formatter))
  }
  implicit val personDTOWrites = new Writes[PersonDTO] {
    def writes(personDTO: PersonDTO) = Json.obj(
      "email" -> personDTO.email,
      "personType" -> personDTO.personType,
      "availability" -> personDTO.availability)
  }
  val jodaDateReads = Reads[DateTime](js =>
    js.validate[String].map[DateTime](dtString =>
      DateTime.parse(dtString, formatter)))

  val durationReads: Reads[Duration] =
    ((JsPath \ "from").read[DateTime](jodaDateReads) and
      (JsPath \ "to").read[DateTime](jodaDateReads))(Duration.apply _)

  val durationSeqReads: Reads[Seq[Duration]] = Reads.seq(durationReads)

  val personDTOReads: Reads[PersonDTO] = (
    (JsPath \ "email").read[String] and
    (JsPath \ "personType").read[PersonType.PersonType](personTypeReadsWrites) and
    (JsPath \ "availability").read[Seq[Duration]](durationSeqReads))(PersonDTO.apply _)
}
