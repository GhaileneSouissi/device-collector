
name := """device-collector"""
scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.11.12", "2.12.8")
concurrentRestrictions in Global += Tags.exclusive(Tags.Test)


lazy val api = project
  .enablePlugins(PlayScala, RpmDeployPlugin, SystemVPlugin)
  .configs(Test)
  .settings(
    name := "device-collector-api",
    libraryDependencies ++= Seq(
      guice,
      ws,
      "io.swagger"               %% "swagger-play2"           % "1.7.1",
      "io.spray"                 %% "spray-json"              % "1.3.6",

      "com.typesafe.slick"       %% "slick"                   % "3.3.0",
      "com.typesafe.slick"       %% "slick-hikaricp"          % "3.3.0",
      "com.typesafe"             % "config"                   % "1.4.1",

      "org.postgresql"           % "postgresql"               % "9.4-1206-jdbc42",
      "org.apache.commons"       % "commons-dbcp2"            % "2.0.1",

      "com.github.stijndehaes"   %% "play-prometheus-filters" % "0.4.0",

      "org.scalatestplus.play"   %% "scalatestplus-play"       % "3.0.0" % Test,
      "org.scalatest"            %% "scalatest"                % "3.0.1" % Test


)
  )

lazy val `device-collector-api` = (project in file("."))
  .aggregate(api)