<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <title>Login</title>

    <!-- Bootstrap core CSS -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
</head>
<body>
    <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
        <a class="navbar-brand" href="<c:out value='/'/>">Get Started</a>
        <sec:authorize access="!isAuthenticated()">
            <a class="navbar-brand" href="<c:out value='login'/>">Login</a>
        </sec:authorize>
    </nav>

    <h1>Login</h1>

    <c:if test="${not empty param.error}">
        <div class="alert alert-danger" role="alert">
            Invalid username or password.
        </div>
    </c:if>

    <div class="container-sm">
        <form:form action="perform_login" method="post">

                <div class="mb-3">
                    <label for="username">User Name :</label>
                    <input type="text" class="form-control" name="username" id="username"/>
                </div>
                <div class="mb-3">
                    <label for="password">Password :</label>
                    <input type="password" class="form-control" name="password" id="password"/>
                </div>
                <input type="submit" class="btn btn-lg btn-primary" role="button" value="Login"/>
                <a href="<c:out value='/password'/>">Forgot password</a>
        </form:form>
        Not registered? <a href="<c:out value='/account'/>">Create an account</a>
    </div>

</body>
</html>