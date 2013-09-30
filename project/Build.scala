import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "AdimissionsDistribution"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "mysql" % "mysql-connector-java" % "5.1.18"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
      
    // 添加公用资源文件夹的静态访问
    playAssetsDirectories <+= baseDirectory / "datas/default/logos",
	playAssetsDirectories <+= baseDirectory / "datas/default/templates"
  )

}
