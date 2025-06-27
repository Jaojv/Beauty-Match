// Gerenciador de Perfil do Usuário
class GerenciadorPerfil {
    constructor() {
        this.modoEdicao = false;
        this.dadosOriginais = {};
        this.inicializar();
    }

    inicializar() {
        this.configurarEventos();
        this.carregarDadosUsuario();
    }

    configurarEventos() {
        // Botão de perfil para abrir modal
        const botaoPerfil = document.querySelector('.botao_logado');
        const modalPerfil = document.getElementById('modal-perfil');
        const fecharModal = document.getElementById('fechar-modal');
        const botaoEditar = document.getElementById('editar-perfil');
        const inputImagem = document.getElementById('input-imagem-perfil');

        if (botaoPerfil && modalPerfil) {
            botaoPerfil.addEventListener('click', (e) => {
                e.preventDefault();
                this.abrirModal();
            });
        }

        if (fecharModal) {
            fecharModal.addEventListener('click', () => {
                this.fecharModal();
            });
        }

        // Fechar modal ao clicar fora
        window.addEventListener('click', (e) => {
            if (e.target === modalPerfil) {
                this.fecharModal();
            }
        });

        if (botaoEditar) {
            botaoEditar.addEventListener('click', () => {
                this.alternarModoEdicao();
            });
        }

        if (inputImagem) {
            inputImagem.addEventListener('change', (e) => {
                this.alterarImagemPerfil(e);
            });
        }

        // Fazer a imagem ser clicável quando estiver em modo de edição
        const fotoPerfil = document.getElementById('foto-perfil');
        if (fotoPerfil) {
            fotoPerfil.addEventListener('click', () => {
                if (this.modoEdicao && inputImagem) {
                    inputImagem.click();
                }
            });
        }
    }

    carregarDadosUsuario() {
        const userData = localStorage.getItem('userData');
        if (userData) {
            const usuario = JSON.parse(userData);
            this.preencherDadosPerfil(usuario);
        }
    }

    preencherDadosPerfil(usuario) {
        const nomePerfil = document.getElementById('nome-perfil');
        const emailPerfil = document.getElementById('email-perfil');
        const senhaPerfil = document.getElementById('senha-perfil');
        const fotoPerfil = document.getElementById('foto-perfil');
        const fotoPerfilNavbar = document.querySelector('.perfil-img');

        if (nomePerfil) nomePerfil.value = usuario.nome || '';
        if (emailPerfil) emailPerfil.value = usuario.email || '';
        if (senhaPerfil) senhaPerfil.value = '********';

        // Definir imagem de perfil (prioridade: localStorage > padrão)
        let imagemPerfil = 'images/user.png'; // Padrão
        
        if (usuario.imagemPerfil) {
            // Se tem imagem no localStorage, usar ela
            imagemPerfil = usuario.imagemPerfil;
        }

        if (fotoPerfil) fotoPerfil.src = imagemPerfil;
        if (fotoPerfilNavbar) fotoPerfilNavbar.src = imagemPerfil;

        // Salvar dados originais
        this.dadosOriginais = {
            nome: usuario.nome || '',
            email: usuario.email || '',
            imagemPerfil: imagemPerfil
        };
    }

    abrirModal() {
        const modalPerfil = document.getElementById('modal-perfil');
        if (modalPerfil) {
            modalPerfil.style.display = 'flex';
        }
    }

    fecharModal() {
        const modalPerfil = document.getElementById('modal-perfil');
        if (modalPerfil) {
            modalPerfil.style.display = 'none';
            // Se estiver em modo de edição, cancelar edição
            if (this.modoEdicao) {
                this.cancelarEdicao();
            }
        }
    }

    alternarModoEdicao() {
        const botaoEditar = document.getElementById('editar-perfil');
        const nomePerfil = document.getElementById('nome-perfil');
        const emailPerfil = document.getElementById('email-perfil');
        const senhaPerfil = document.getElementById('senha-perfil');
        const containerImagem = document.getElementById('container-imagem-perfil');
        const fotoPerfil = document.getElementById('foto-perfil');

        if (!this.modoEdicao) {
            // Entrar no modo de edição
            this.modoEdicao = true;
            
            if (botaoEditar) botaoEditar.textContent = 'Salvar';
            if (nomePerfil) nomePerfil.readOnly = false;
            if (emailPerfil) emailPerfil.readOnly = false;
            if (senhaPerfil) {
                senhaPerfil.readOnly = false;
                senhaPerfil.value = ''; // Limpar senha para nova entrada
                senhaPerfil.placeholder = 'Nova senha (deixe em branco para não alterar)';
            }
            if (containerImagem) containerImagem.style.display = 'block';
            if (fotoPerfil) {
                fotoPerfil.style.cursor = 'pointer';
                fotoPerfil.title = 'Clique para alterar a foto';
            }

        } else {
            // Salvar alterações
            this.salvarAlteracoes();
        }
    }

    cancelarEdicao() {
        const botaoEditar = document.getElementById('editar-perfil');
        const nomePerfil = document.getElementById('nome-perfil');
        const emailPerfil = document.getElementById('email-perfil');
        const senhaPerfil = document.getElementById('senha-perfil');
        const containerImagem = document.getElementById('container-imagem-perfil');
        const fotoPerfil = document.getElementById('foto-perfil');

        this.modoEdicao = false;

        if (botaoEditar) botaoEditar.textContent = 'Editar Perfil';
        if (nomePerfil) {
            nomePerfil.readOnly = true;
            nomePerfil.value = this.dadosOriginais.nome;
        }
        if (emailPerfil) {
            emailPerfil.readOnly = true;
            emailPerfil.value = this.dadosOriginais.email;
        }
        if (senhaPerfil) {
            senhaPerfil.readOnly = true;
            senhaPerfil.value = '********';
            senhaPerfil.placeholder = '';
        }
        if (containerImagem) containerImagem.style.display = 'none';
        if (fotoPerfil) {
            fotoPerfil.style.cursor = 'default';
            fotoPerfil.title = '';
        }

        // Restaurar imagem original
        if (fotoPerfil) {
            fotoPerfil.src = this.dadosOriginais.imagemPerfil;
            // Também restaurar na navbar
            const fotoPerfilNavbar = document.querySelector('.perfil-img');
            if (fotoPerfilNavbar) {
                fotoPerfilNavbar.src = this.dadosOriginais.imagemPerfil;
            }
        }
    }

    async salvarAlteracoes() {
        const nomePerfil = document.getElementById('nome-perfil');
        const emailPerfil = document.getElementById('email-perfil');
        const senhaPerfil = document.getElementById('senha-perfil');
        const botaoEditar = document.getElementById('editar-perfil');

        const dadosAtualizados = {
            nome: nomePerfil ? nomePerfil.value.trim() : '',
            email: emailPerfil ? emailPerfil.value.trim() : '',
            senha: senhaPerfil && senhaPerfil.value ? senhaPerfil.value : null
        };

        // Validações básicas
        if (!dadosAtualizados.nome) {
            alert('Nome é obrigatório');
            return;
        }

        if (!dadosAtualizados.email) {
            alert('Email é obrigatório');
            return;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(dadosAtualizados.email)) {
            alert('Email inválido');
            return;
        }

        if (dadosAtualizados.senha && dadosAtualizados.senha.length < 6) {
            alert('Senha deve ter pelo menos 6 caracteres');
            return;
        }

        try {
            // Mostrar loading
            if (botaoEditar) {
                botaoEditar.textContent = 'Salvando...';
                botaoEditar.disabled = true;
            }

            // Atualizar dados no localStorage
            const userData = JSON.parse(localStorage.getItem('userData'));
            userData.nome = dadosAtualizados.nome;
            userData.email = dadosAtualizados.email;
            if (dadosAtualizados.senha) {
                // Não salvar senha no localStorage por segurança
                console.log('Senha alterada (não salva no localStorage por segurança)');
            }
            localStorage.setItem('userData', JSON.stringify(userData));

            // Atualizar dados originais
            this.dadosOriginais.nome = dadosAtualizados.nome;
            this.dadosOriginais.email = dadosAtualizados.email;

            // Sair do modo de edição
            this.modoEdicao = false;
            if (botaoEditar) {
                botaoEditar.textContent = 'Editar Perfil';
                botaoEditar.disabled = false;
            }

            // Tornar campos readonly novamente
            if (nomePerfil) nomePerfil.readOnly = true;
            if (emailPerfil) emailPerfil.readOnly = true;
            if (senhaPerfil) {
                senhaPerfil.readOnly = true;
                senhaPerfil.value = '********';
                senhaPerfil.placeholder = '';
            }

            // Esconder container de imagem
            const containerImagem = document.getElementById('container-imagem-perfil');
            if (containerImagem) containerImagem.style.display = 'none';

            // Remover cursor pointer da imagem
            const fotoPerfil = document.getElementById('foto-perfil');
            if (fotoPerfil) {
                fotoPerfil.style.cursor = 'default';
                fotoPerfil.title = '';
            }

            alert('Perfil atualizado com sucesso!');

        } catch (error) {
            console.error('Erro ao atualizar perfil:', error);
            alert('Erro ao atualizar perfil: ' + error.message);
            
            // Restaurar botão
            if (botaoEditar) {
                botaoEditar.textContent = 'Salvar';
                botaoEditar.disabled = false;
            }
        }
    }

    alterarImagemPerfil(event) {
        const arquivo = event.target.files[0];
        if (!arquivo) return;

        // Validar tipo de arquivo
        if (!arquivo.type.startsWith('image/')) {
            alert('Por favor, selecione apenas arquivos de imagem');
            return;
        }

        // Validar tamanho (máximo 2MB para localStorage)
        if (arquivo.size > 2 * 1024 * 1024) {
            alert('A imagem deve ter no máximo 2MB para ser salva localmente');
            return;
        }

        const leitor = new FileReader();
        leitor.onload = (e) => {
            const imagemBase64 = e.target.result;
            
            // Atualizar preview imediatamente
            const fotoPerfil = document.getElementById('foto-perfil');
            if (fotoPerfil) {
                fotoPerfil.src = imagemBase64;
            }

            // Atualizar imagem na navbar também
            const fotoPerfilNavbar = document.querySelector('.perfil-img');
            if (fotoPerfilNavbar) {
                fotoPerfilNavbar.src = imagemBase64;
            }

            // Salvar no localStorage
            this.salvarImagemNoLocalStorage(imagemBase64);
        };
        leitor.readAsDataURL(arquivo);
    }

    salvarImagemNoLocalStorage(imagemBase64) {
        try {
            // Atualizar dados no localStorage
            const userData = JSON.parse(localStorage.getItem('userData'));
            userData.imagemPerfil = imagemBase64;
            localStorage.setItem('userData', JSON.stringify(userData));

            // Atualizar dados originais
            this.dadosOriginais.imagemPerfil = imagemBase64;

            console.log('Imagem de perfil salva no localStorage com sucesso');
        } catch (error) {
            console.error('Erro ao salvar imagem no localStorage:', error);
            alert('Erro ao salvar imagem: ' + error.message);
        }
    }
}

// Inicializar gerenciador quando o DOM estiver pronto
document.addEventListener('DOMContentLoaded', function() {
    new GerenciadorPerfil();
});