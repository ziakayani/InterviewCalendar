package model

import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json.Writes

case class ResponseDTO(email: String, personType: PersonType.PersonType, overlapped: Seq[PersonDTO])

object ResponseDTO {

  // Custom writer for JSON
  implicit val ResponseDTOWrites = new Writes[ResponseDTO] {
    def writes(responseDTO: ResponseDTO) = Json.obj(
      "email" -> responseDTO.email,
      "personType" -> responseDTO.personType,
      "overlapped" -> responseDTO.overlapped)
  }
}