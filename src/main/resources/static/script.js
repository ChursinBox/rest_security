let currentUserId = null;
let roles = [];

function fetchRoles() {
    fetch('/api/roles')
        .then(response => response.json())
        .then(data => {
            roles = data;
        });
}

$(document).ready(function() {
    fetchUsers();
    fetchRoles();
});

function fetchUsers() {
    fetch('/api/all')
        .then(response => response.json())
        .then(users => {
            const tbody = document.getElementById('usersTableBody');
            tbody.innerHTML = ''; // Очистка таблицы
            users.forEach(user => {
                const row = `<tr>
                            <td>${user.id}</td>
                            <td>${user.username}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${user.password}</td>
                            <td>${user.roles.map(role => role.roleName).join(', ')}</td>
                            <td>
                                <button class="btn btn-warning btn-sm" onclick="openEditUserModal(${user.id})">Edit</button>
                                <button class="btn btn-danger btn-sm" onclick="deleteUser(${user.id})">Delete</button>
                            </td>
                        </tr>`;
                tbody.innerHTML += row;
            });
        });
}

function openAddUserModal() {
    currentUserId = null;
    $('#username').val('');
    $('#age').val('');
    $('#email').val('');
    $('#password').val('');
    $('#role').empty();
    roles.forEach(role => {
        $('#role').append(new Option(role.roleName, role.id));
    });
    $('#userModal').modal('show');
}

function openEditUserModal(userId) {
    currentUserId = userId;
    fetch(`/api/all/${userId}`)
        .then(response => response.json())
        .then(user => {
            $('#username').val(user.username);
            $('#age').val(user.age);
            $('#email').val(user.email);
            $('#password').val('');

            const roleSelect = $('#role');
            roleSelect.empty();
            roles.forEach(role => {
                const option = new Option(role.roleName, role.id);
                if (user.roles.some(userRole => userRole.id === role.id)) {
                    option.selected = true;
                }
                roleSelect.append(option);
            });

            $('#userModal').modal('show');
        });
}

function submitUserForm() {
    const user = {
        username: $('#username').val(),
        age: $('#age').val(),
        email: $('#email').val(),
        password: $('#password').val(),
        roles: [{ id: $('#role').val() }]
    };

    const method = currentUserId ? 'PUT' : 'POST';
    const url = currentUserId ? `/api/all/${currentUserId}` : '/api/all';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (response.ok) {
                fetchUsers();
                $('#userModal').modal('hide');
            }
        });
}

function deleteUser(userId) {
    if (confirm('Are you sure you want to delete this user?')) {
        fetch(`/api/all/${userId}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    fetchUsers();
                }
            });
    }
}