document.getElementById('email-input-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const submitBtn = document.getElementById('email-input-form-button');
    const loader = document.getElementById('loader');
    const notification = document.getElementById('notification');
    
    const formData = {
        username: document.getElementById('name-input').value,
        email: document.getElementById('email-input').value
    };
    
    submitBtn.disabled = true;
    loader.style.display = 'block';
    notification.innerHTML = '';  
    
    try {
        const response = await fetch('https://ogmamain.loca.lt/register', {
            method: 'POST',                  
            headers: {
                'Content-Type': 'application/json;charset=utf-8',
            },
            body: JSON.stringify(formData) 
        });
        
        if (response.ok) {
            showNotification('Данные успешно отправлены!', 'success');
            document.getElementById('email-input-form').reset();
        } else {
            showNotification(`Ошибка: ${result.message || 'Неизвестная ошибка'}`, 'error');
        }
    } catch (error) {
        showNotification(`Ошибка соединения: ${error.message}`, 'error');
    } finally {
        loader.style.display = 'none';
        submitBtn.disabled = false;
    }
});

function showNotification(message, type) {
    const notification = document.getElementById('notification');
    notification.innerHTML = message;                  
    notification.className = 'notification ' + type;
}