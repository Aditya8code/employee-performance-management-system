<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <footer class="bg-white border-top py-3 text-center text-muted small mt-auto">
        <div class="container-fluid">
            <span>&copy; <%= java.time.Year.now().getValue() %> Employee Performance Management System (EPS) &middot; Enterprise Grade</span>
        </div>
    </footer>

    <!-- Bootstrap 5 Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Application JavaScript -->
    <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
</body>
</html>
