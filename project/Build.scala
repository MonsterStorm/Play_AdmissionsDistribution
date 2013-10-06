import sbt._
import Keys._
import play.Project._
//import play2war
import com.github.play2war.plugin._

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

  val main = play.Project(appName, appVersion, appDependencies)
  .settings(Play2WarPlugin.play2WarSettings: _*)
  .settings(
    // Add your own project settings here
      
    // 添加公用资源文件夹的静态访问
    //默认头像
    playAssetsDirectories <+= baseDirectory / "datas/default/logos",
    //用户头像
    playAssetsDirectories <+= baseDirectory / "datas/logos",
    //默认模板
	playAssetsDirectories <+= baseDirectory / "datas/default/templates",
	//广告图片
	playAssetsDirectories <+= baseDirectory / "datas/advertisments",
	
	//play war
	Play2WarKeys.servletVersion := "3.0"
  )
}
