const menuIcon = document.getElementById('menu-icon');
const menu = document.getElementById('menu');

menuIcon.addEventListener('click', () => {
    if (menu.style.display === 'block') {
        menu.style.opacity = '0';
        setTimeout(() => {
            menu.style.display = 'none';
        }, 300); // Match the transition duration
    } else {
        menu.style.display = 'block';
        setTimeout(() => {
            menu.style.opacity = '1';
        }, 10); // Small delay to trigger transition
    }
});
