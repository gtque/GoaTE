<?php
$href_path = "";
$_uri = getenv('REQUEST_URI'); //= "rule1/forms/rule1.xml";
$form = "";
if(strpos($_uri, "flow.php") !== false){
	$href_path = "..";
}
if(!isset($theform)) {
	//echo "_uri: ".$_uri."\n<br/>";
	$_uri = str_replace("/flow.php/", "", $_uri);
	$_uri = str_replace("flow.php/", "", $_uri);
	$_uri = str_replace("/flow.php", "", $_uri);
	$_uri = str_replace("flow.php", "", $_uri);
	$_uri = explode("?", $_uri)[0];
	$_last = substr($_uri, -1);
	if(strcmp($_last, $_uri)==0){
		$_uri = "home";
		//echo("setting home\n</br>");
	}
	$theform = "forms/form.xml";//s/".$_uri.".xml";
	$form = $_uri;
	//echo("from:".$form."\n<br/>");
}

//echo "theform: ".$theform."\n<br/>";
//chdir("../..");
$thePath = getcwd() . "/";
$inproduction = "true";
include $thePath.'flow/api/webpageapi/php/flow.php';
?>