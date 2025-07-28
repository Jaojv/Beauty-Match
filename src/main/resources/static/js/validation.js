// Sistema de validação frontend para formulários
// Gerencia validações de email, senha e campos obrigatórios
class ValidationManager {
    constructor() {
        this.setupValidations();
    }

    // Configurar validações para formulários
    // Adiciona event listeners para validação em tempo real
    setupValidations() {
        // Validação de email
        const emailInputs = document.querySelectorAll('input[type="email"]');
        emailInputs.forEach(input => {
            input.addEventListener('blur', () => this.validateEmail(input));
            input.addEventListener('input', () => this.clearError(input));
        });

        // Validação de senha
        const passwordInputs = document.querySelectorAll('input[type="password"]');
        passwordInputs.forEach(input => {
            input.addEventListener('blur', () => this.validatePassword(input));
            input.addEventListener('input', () => this.clearError(input));
        });

        // Validação de campos obrigatórios
        const requiredInputs = document.querySelectorAll('input[required]');
        requiredInputs.forEach(input => {
            input.addEventListener('blur', () => this.validateRequired(input));
            input.addEventListener('input', () => this.clearError(input));
        });
    }

    // Validar email usando regex
    validateEmail(input) {
        const email = input.value.trim();
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        
        if (email && !emailRegex.test(email)) {
            this.showError(input, 'Email inválido');
            return false;
        }
        
        this.clearError(input);
        return true;
    }

    // Validar senha (mínimo 6 caracteres)
    validatePassword(input) {
        const password = input.value;
        
        if (password && password.length < 6) {
            this.showError(input, 'Senha deve ter pelo menos 6 caracteres');
            return false;
        }
        
        this.clearError(input);
        return true;
    }

    // Validar campo obrigatório
    validateRequired(input) {
        const value = input.value.trim();
        
        if (!value) {
            this.showError(input, 'Este campo é obrigatório');
            return false;
        }
        
        this.clearError(input);
        return true;
    }

    // Validar formulário completo
    // Verifica todos os campos obrigatórios de uma vez
    validateForm(form) {
        const inputs = form.querySelectorAll('input[required]');
        let isValid = true;

        inputs.forEach(input => {
            if (input.type === 'email') {
                if (!this.validateEmail(input)) isValid = false;
            } else if (input.type === 'password') {
                if (!this.validatePassword(input)) isValid = false;
            } else {
                if (!this.validateRequired(input)) isValid = false;
            }
        });

        return isValid;
    }

    // Mostrar erro visual no campo
    showError(input, message) {
        this.clearError(input);
        
        // Cria elemento de erro
        const errorDiv = document.createElement('div');
        errorDiv.className = 'error-message';
        errorDiv.textContent = message;
        errorDiv.style.color = 'red';
        errorDiv.style.fontSize = '12px';
        errorDiv.style.marginTop = '5px';
        
        input.parentNode.appendChild(errorDiv);
        input.style.borderColor = 'red';
    }

    // Limpar erro visual do campo
    clearError(input) {
        const errorDiv = input.parentNode.querySelector('.error-message');
        if (errorDiv) {
            errorDiv.remove();
        }
        input.style.borderColor = '';
    }

    // Sanitizar input (proteção básica contra XSS)
    // Remove caracteres perigosos como < e >
    sanitizeInput(input) {
        return input.replace(/[<>]/g, '');
    }
}

// Instância global do gerenciador de validação
// Usada por todos os formulários da aplicação
const validationManager = new ValidationManager(); 