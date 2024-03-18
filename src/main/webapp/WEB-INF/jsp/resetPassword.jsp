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

<h1>Reset Password</h1>

<c:if test="${not empty param.reset}" >
    <div class="alert alert-success" role="alert">
        Your password has been reset!
    </div>
</c:if>
<form:form method="post" modelAttribute="password">
    <div class="container-sm">
        <div class="mb-3">
            <label for="password">Password :</label>
            <form:password path="password" id="password" class="form-control"/>
            <form:errors path="password" cssClass="error" />
        </div>
        <div class="mb-3">
            <label for="passwordconfirm">Confirm Password :</label>
            <form:password path="matchingPassword" id="passwordconfirm" class="form-control"/>
            <form:errors path="matchingPassword" cssClass="error" />
        </div>
        <form:hidden path="username" />
        <input type="submit" class="btn btn-lg btn-primary" role="button" value="Reset Password"/>
    </div>
</form:form>
</body>
</html>