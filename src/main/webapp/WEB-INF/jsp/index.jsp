<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <title>Recipe Book App</title>

    <!-- Bootstrap core CSS -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>
    <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
        <a class="navbar-brand" href="<c:out value='/'/>">Get Started</a>
        <sec:authorize access="!isAuthenticated()">
            <a class="navbar-brand" href="<c:out value='login'/>">Login</a>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <a class="navbar-brand" href="<c:out value='recipes'/>">Recipes</a>
            <a class="navbar-brand" href="<c:out value='logout'/>">Logout</a>
        </sec:authorize>
    </nav>
    <h1>Get Started</h1>
    <sec:authorize access="!isAuthenticated()">
        You are not logged in into the system. Please login before you can access the recipes.
    </sec:authorize>
    <sec:authorize access="isAuthenticated()">
        Welcome Back, <sec:authentication property="name" />
    </sec:authorize>
</body>
</html>