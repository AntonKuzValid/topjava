<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Meals</title>
    <style>
        .red {
            color: red;
        }

        .green {
            color: green;
        }

        td {
            padding: 10px;
        }

        tr {
            background-color: #F2F2F2;
            border: solid darkgray;
        }

        table {
            border-collapse: collapse;
        }

        tr:hover {
            background-color: lightgoldenrodyellow;
        }

        input[type="submit"]:hover {
            background-color: lightcyan;
        }

        input[type="submit"] {
            padding: 8px;
            width: 100px;
            border-radius: 10px;
            box-shadow: darkgray;
        }

        input:focus {
            border-color: #0088cc;
        }
    </style>
</head>

<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meal list -</h2>
<table>
    <c:forEach items="${mealList}" var="meal">
        <tr class=<c:out value="${meal.isExceed()==true? 'red' : 'green'}"/>>
            <td>${meal.id}</td>
            <td> ${meal.dateTime.toLocalDate()}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meal?action=isupdate&mealIdForUpdate=${meal.id}" s><img src="Edit.png" width="50" height="50"
                                                                                 alt="Update"/> </a></td>
            <td><a href="meal?action=delete&mealIdForDelete=${meal.id}"><img src="Delete.png" width="30" height="30"
                                                                             alt="Delete"/></a></td>
        </tr>
    </c:forEach>
</table>
<br>

<h2><c:out value="${isUpdate? 'Pease put data below to UPDATE meal':'ADD meal'}"/></h2>

<form
        <c:if test="${isUpdate==null}">action="meal?action=add"</c:if>
        <c:if test="${isUpdate!=null}">action="meal?action=update"</c:if>
        method="post">
    <table>
        <tr>
            <td><label for="date-field">Date</label></td>
            <td><input type="datetime-local" name="Date" id="date-field" required></td>
        </tr>
        <tr>
            <td><label for="description-field">Description </label></td>
            <td><input type="text" name="Description" id="description-field" required
                       <c:if test="${isUpdate!=null}">placeholder="${updateDesc}"</c:if>></td>
        </tr>
        <tr>
            <td><label for="calories-field">Calories </label></td>
            <td><input type="number" name="Calories" id="calories-field" required
                       <c:if test="${isUpdate!=null}">placeholder="${updateCal}"</c:if>></td>
        </tr>
        <c:if test="${isUpdate}">
            <tr>
                <td colspan="2"><input type="submit" value="Update">
                    <input type="hidden" name="mealIdForUpdate" value="${mealIdForUpdate}">
                    <input type="hidden" name="isUpdate" value="${isUpdate}">
                </td>
            </tr>
        </c:if>
        <c:if test="${!isUpdate}">
            <tr>
                <td colspan="2"><input type="submit" value="Add"></td>
            </tr>
        </c:if>
    </table>
</form>
</body>
</html>
