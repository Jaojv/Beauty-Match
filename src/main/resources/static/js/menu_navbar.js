const menuIcon = document.getElementById('menu-icon');
const menu = document.getElementById('menu');

menuIcon.addEventListener('click', () => {
    if (menu.style.display === 'block') {
        menu.style.opacity = '0';
        menuIcon.classList.remove('active');
        setTimeout(() => {
            menu.style.display = 'none';
        }, 300);
    } else {
        menu.style.display = 'block';
        setTimeout(() => {
            menu.style.opacity = '1';
            menuIcon.classList.add('active');
        }, 10);
    }
});