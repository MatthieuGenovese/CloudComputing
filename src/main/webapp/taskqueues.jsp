<!DOCTYPE html>
<!--
  Copyright 2016 Google Inc.
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <style type="text/css">

        body
        {
            background-color : black;
            color : white
        }


        h1
        {
            text-align: center;
        }

        h2
        {
            color : red;
        }

        alignLeft
        {
           padding : 2cm;
        }

        div
        {
            width: 50%;
            margin: auto;
            margin-top : 10%;
        }

    </style>
</head>

<body>
<h1>Bienvenue sur notre site de convertisseur vidéo </h1>

<div>
<h2>Identifiez-vous</h2>
  <alignLeft>
   Username: <input type="text" name"username" value="Jean Michel"><br>
  </alignLeft>
<h2>Mettez votre video</h2>
    <alignLeft>
        Vidéo: <input type="text" name"video" value="Video1"><br><br>
    </alignLeft>
    <alignLeft>
        Taille de la Vidéo: <input type="text" name"taille" value="30"><br><br>
    </alignLeft>

    <form action = "http://sacc-liechtensteger-182811.appspot.com/upload" method="post" enctype ="multipart/raw">
        <input type="submit"  value="Convertir"/>
    </form>

    ${message}
</div>



</body>
</html>