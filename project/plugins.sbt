addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")
lazy val root = (project in file(".")).dependsOn(gitlabPlugin)
lazy val gitlabPlugin = RootProject(uri("git://github.com/gilcloud/sbt-gitlab"))
