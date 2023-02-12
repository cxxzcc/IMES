# 如果输入格式不对，给出提示！
tips() {
	echo ""
	echo "WARNING!!!......Tips, please use command: sh auto_restart.sh -f [module_name].  log"
	echo "-------For example:---------------------------"
	echo "
        sh /root/auto_restart.sh andon
        sh /root/auto_restart.sh system
        sh /root/auto_restart.sh auth
        sh /root/auto_restart.sh notice
        sh /root/auto_restart.sh me
        sh /root/auto_restart.sh pp
        sh /root/auto_restart.sh mes
        sh /root/auto_restart.sh xxl
        sh /root/auto_restart.sh md
        sh /root/auto_restart.sh form
        sh /root/auto_restart.sh wms-portal
        sh /root/auto_restart.sh wms-md
        sh /root/auto_restart.sh wm
        sh /root/auto_restart.sh iow
        "
  echo "------------------------------------------------"
	exit 1
}


# 启动方法
restart() {

	echo ".............................Restarting.............................."
		# 重新获取一下pid，因为其它操作如start会导致pid的状态更新
	pid=`jcmd | grep $JAR_NAME | awk '{print $1}'`
	pid2=`jcmd | grep $JAR_NAME2 | awk '{print $1}'`
        # -z 表示如果$pid为空时执行。 注意：每个命令和变量之间一定要前后加空格，否则会提示command找不到
	if [ ! -z $pid ]; then
		kill $pid
	fi
	if [ ! -z $pid2 ]; then
		kill $pid2
	fi


        # 重新获取一下pid，因为其它操作如stop会导致pid的状态更新
	pid=`jcmd | grep $JAR_NAME | awk '{print $1}'`
	pid2=`jcmd | grep $JAR_NAME2 | awk '{print $1}'`
        # -z 表示如果$pid为空时执行
	if [ -z $pid ]; then
        nohup java  -jar -Xms256m -Xmx512m $JAR_NAME  &
		pid=`jcmd | grep $JAR_NAME | awk '{print $1}'`
        echo "Service ${JAR_NAME} is starting！pid=${pid}"
		echo "........................Restart successfully！........................."
	else
	  restart
	fi
}

restartForLog() {

	echo ".............................restartForLog.............................."
		# 重新获取一下pid，因为其它操作如start会导致pid的状态更新
	pid=`jcmd | grep $JAR_NAME | awk '{print $1}'`
	pid2=`jcmd | grep $JAR_NAME2 | awk '{print $1}'`
        # -z 表示如果$pid为空时执行。 注意：每个命令和变量之间一定要前后加空格，否则会提示command找不到
	if [ ! -z $pid ]; then
		kill $pid
	fi
	if [ ! -z $pid2 ]; then
		kill $pid2
	fi


        # 重新获取一下pid，因为其它操作如stop会导致pid的状态更新
	pid=`jcmd | grep $JAR_NAME | awk '{print $1}'`
	pid2=`jcmd | grep $JAR_NAME2 | awk '{print $1}'`
        # -z 表示如果$pid为空时执行
	if [ -z $pid ]; then
         java  -jar -Xms256m -Xmx512m $JAR_NAME
		pid=`jcmd | grep $JAR_NAME | awk '{print $1}'`
        echo "Service ${JAR_NAME} is starting！pid=${pid}"
		echo "........................restartForLog successfully！........................."
		restart
	else
	  restartForLog
	fi
}



All() {
  cd /usr/local/itl/service

  for i in $(jcmd  | awk '{print $1"+"$2}');do

if [ ${i#*+} != "sun.tools.jcmd.JCmd" ]; then
      echo "${i%+*}${i#*+}"
      kill ${i%+*}
      nohup java  -jar -Xms256m -Xmx512m ${i#*+}  &

      fi

  done




}


export MODE=""
export FUNCTION_MODE=""




while getopts ":m:f" opt
do
    case $opt in
        m)
            MODE=$OPTARG;;
        f)
            FUNCTION_MODE=$OPTARG;;
        s)
            SERVER=$OPTARG;;
        c)
            MEMBER_LIST=$OPTARG;;
        p)
            EMBEDDED_STORAGE=$OPTARG;;
        ?)
        echo "Unknown parameter"
        exit 1;;
    esac
done




# 根据输入参数执行对应方法，不输入则执行tips提示方法
case "$1" in
    "All")
     All
     ;;
   "andon")
    	JAR_NAME="/www/server/sebservice/mes-andon-provider-1.0.jar"
		JAR_NAME2="mes-andon-provider-1.0.jar"
      if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   "wm")
    	JAR_NAME="/www/server/sebservice/wms-wm-provider-1.0.jar"
		JAR_NAME2="wms-wm-provider-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
     "label")
    	JAR_NAME="/www/server/sebservice/mom-label-provider-1.0.jar"
		JAR_NAME2="mom-label-provider-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   "system")
		JAR_NAME="/www/server/sebservice/iap-system-provider-1.0.jar"
		JAR_NAME2="iap-system-provider-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   "auth")
   JAR_NAME="/www/server/sebservice/iap-auth-provider.jar"
		JAR_NAME2="iap-auth-provider.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   "notice")
   JAR_NAME="/www/server/sebservice/iap-notice-provider-1.0.jar"
		JAR_NAME2="iap-notice-provider-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
    "me")
		JAR_NAME="/www/server/sebservice/mes-me-provider-1.0.jar"
		JAR_NAME2="mes-me-provider-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   "pp")
		JAR_NAME="/www/server/sebservice/mes-pp-provider-1.0.jar"
		JAR_NAME2="mes-pp-provider-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   "mes")
		JAR_NAME="/www/server/sebservice/iap-mes-provider-1.0.jar"
		JAR_NAME2="iap-mes-provider-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   "xxl")
			JAR_NAME="/www/server/sebservice/iap-xxl-job-admin-1.0.jar"
		JAR_NAME2="iap-xxl-job-admin-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   "md")
				JAR_NAME="/www/server/sebservice/mes-md-provider-1.0.jar"
		JAR_NAME2="mes-md-provider-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   "form")
				JAR_NAME="/www/server/sebservice/mom-form-provider-1.0.jar"
		JAR_NAME2="mom-form-provider-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   "wms-portal")
				JAR_NAME="/www/server/sebservice/wms-portal-provider-1.0.jar"
		JAR_NAME2="wms-portal-provider-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   "wms-md")
				JAR_NAME="/www/server/sebservice/wms-md-provider-1.0.jar"
		JAR_NAME2="wms-md-provider-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   "iow")
				JAR_NAME="/www/server/sebservice/wms-iow-provider-1.0.jar"
		JAR_NAME2="wms-iow-provider-1.0.jar"
     if [[ "$2" == "log" ]]; then
           restartForLog
      else
          restart
      fi
     ;;
   *)
     tips
     ;;
esac
