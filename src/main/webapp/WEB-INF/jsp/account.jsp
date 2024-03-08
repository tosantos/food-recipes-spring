<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <title>Create Account</title>

    <!-- Bootstrap core CSS -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">

    <style>
        .error {
            color: #ff0000;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
    <a class="navbar-brand" href="<c:out value='/'/>">Get Started</a>
    <sec:authorize access="!isAuthenticated()">
        <a class="navbar-brand" href="<c:out value='login'/>">Login</a>
    </sec:authorize>
</nav>

<h1>Create Account</h1>

<form:form action="account" method="post" modelAttribute="account">
    <div class="container-sm">
        <div class="mb-3">
            <label for="username">User Name :</label>
            <form:input path="username" id="username" class="form-control"/>
            <form:errors path="username" cssClass="error" />
        </div>
        <div class="mb-3">
            <label for="firstname">First Name :</label>
            <form:input path="firstname" id="firstname" class="form-control"/>
            <form:errors path="firstname" cssClass="error" />
        </div>
        <div class="mb-3">
            <label for="lastname">Last Name :</label>
            <form:input path="lastname" id="lastname" class="form-control"/>
            <form:errors path="lastname" cssClass="error" />
        </div>
        <div class="mb-3">
            <label for="email">Email :</label>
            <form:input path="email" id="email" class="form-control"/>
            <form:errors path="email" cssClass="error" />
        </div>
        <div class="mb-3">
            <label for="password">Password :</label>
            <form:password path="password" id="password" class="form-control"/>
            <form:errors path="password" cssClass="error" />
        </div>
        <div class="mb-3">
            <label for="passwordconfirm">Confirm Password :</label>
            <form:password path="passwordconfirm" id="passwordconfirm" class="form-control"/>
            <form:errors path="passwordconfirm" cssClass="error" />
        </div>
        <input type="submit" class="btn btn-lg btn-primary" role="button" value="Create"/>
    </div>
</form:form>
</body>
</html>