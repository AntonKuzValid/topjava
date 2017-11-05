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
        tr{
            background-color: lightgrey;
            border: solid darkgray;
        }
        table {
            border-collapse: collapse;
        }

        tr:hover{
            background-color: lightgoldenrodyellow;
        }
        input:hover{
            background-color: lightcyan;
        }
        input[type="submit"]{
            padding: 8px;
            width: 100px;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meal list -</h2>
<table>
    <c:forEach items="${mealWithExceeds}" var="meal">
        <tr class=<c:out value="${meal.isExceed()==true? 'red' : 'green'}"/>>
            <td>${meal.id}</td>
            <td> ${meal.dateTime.toLocalDate()}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>
                <form action="/meal" method="post">
                    <input type="hidden" name="mealIdForEdit" value="${meal.id}">
                    <input type="submit" value="Edit"></form>
            </td>
            <td>
                <form action="/meal" method="post">
                    <input type="hidden" name="mealIdForRemove" value="${meal.id}">
                    <input type="submit" value="Remove"></form>
            </td>
        </tr>
    </c:forEach>
</table>
<br>

<h2><c:out value="${isEdit? 'Edit meal':'Add meal'}"/></h2>

<form action="/meal" method="post">
    <table>
        <tr>
            <td><label for="date-field">Date</label></td>
            <td><input type="datetime-local" name="Date" id="date-field"></td>
        </tr>
        <tr>
            <td><label for="description-field">Description </label></td>
            <td><input type="text" name="Description" id="description-field"></td>
        </tr>
        <tr>
            <td><label for="calories-field">Calories </label></td>
            <td><input type="text" name="Calories" id="calories-field"></td>
        </tr>
        <c:if test="${isEdit}">
            <tr>
                <td colspan="2"><input type="submit" value="Edit">
                    <input type="hidden" name="mealId" value="${mealId}">
                    <input type="hidden" name="isEdit" value="${true}">
                </td>
            </tr>
        </c:if>
        <c:if test="${!isEdit}">
            <tr>
                <td colspan="2"><input type="submit" value="Add"></td>
            </tr>
        </c:if>
    </table>
</form>
</body>
</html>
