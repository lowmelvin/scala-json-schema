# Scala Json Schema (Lite)

This is a minimal library to generate JSON schema from Scala ADTs.
It is built on top of [circe](https://circe.github.io/circe/).

To reduce complexity and maintenance, this library
does the absolute minimum: It will traverse your ADT and generate
a schema with nested types and properties. No other fields are included.

Product types get the type `object` and sum types are converted into `anyOf`.

You can inject all other fields via annotation.
As intended, there is zero validation--you can do whatever you want.

## Installation

This library is built for Scala 3.

```scala
libraryDependencies += "com.melvinlow" %% "scala-json-schema" % "<VERSION>"
```

## Examples:

Encode a product:

```scala
import com.melvinlow.json.schema.*
import com.melvinlow.json.schema.generic.auto.given

case class Foo(x: Int, y: String)

JsonSchema[Foo].spaces2
// res0: String = """{
//   "type" : "object",
//   "properties" : {
//     "x" : {
//       "type" : "integer"
//     },
//     "y" : {
//       "type" : "string"
//     }
//   }
// }"""
```

Encode a coproduct:

```scala
enum Bar:
  case A, B

JsonSchema[Bar].spaces2
// res1: String = """{
//   "anyOf" : [
//     {
//       "type" : "object",
//       "properties" : {
//         
//       }
//     },
//     {
//       "type" : "object",
//       "properties" : {
//         
//       }
//     }
//   ]
// }"""
```

Add fields via annotation (it takes as input any `String` and `Json` key-value pair):

```scala
import com.melvinlow.json.schema.annotation.JsonSchemaField
import io.circe.Json
import io.circe.syntax.*

@JsonSchemaField("title", "dog".asJson)
@JsonSchemaField("required", Array("name").asJson)
case class Dog(
  @JsonSchemaField("minLength", 1.asJson)
  name: String
)

JsonSchema[Dog].spaces2
// res2: String = """{
//   "required" : [
//     "name"
//   ],
//   "title" : "dog",
//   "type" : "object",
//   "properties" : {
//     "name" : {
//       "minLength" : 1,
//       "type" : "string"
//     }
//   }
// }"""
```

Create an encoder for a new type:

```scala
object H:
  @JsonSchemaField("description", "my custom int".asJson)
  opaque type MyInt = Int

given JsonSchemaEncoder[H.MyInt] with
  def schema: Json =
    val base = Json.obj("type" -> "integer".asJson)
    val annotations = JsonSchemaField.onType[H.MyInt]
    base.deepMerge(Json.obj(annotations*))

JsonSchema[H.MyInt].spaces2
// res3: String = """{
//   "description" : "my custom int",
//   "type" : "integer"
// }"""
```
