name := "kafka-connect-datatype-smt"

enablePlugins(GitVersioning)
git.useGitDescribe := true

scalaVersion := "2.12.10"
libraryDependencies ++= Seq(
  "org.apache.kafka" % "connect-api" % "2.3.1",
  "org.apache.kafka" % "kafka-clients" % "2.3.1"
)
fork in Test := true
