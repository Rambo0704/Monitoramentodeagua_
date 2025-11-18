
const API_BASE = "http://localhost:8080";
const AUTH_KEY = "monitoramentoAuth";

let auth = { token: null, nome: null };

let modoEdicao = {
    ativo: false,
    id: null
};


async function login() {
    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;
    const status = document.getElementById('loginStatus');

    status.textContent = 'Autenticando...';
    status.classList.remove('text-red-600');
    status.classList.add('text-gray-600');

    try {
        const resp = await fetch(API_BASE + '/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, senha })
        });

        if (!resp.ok) {
            status.textContent = 'Login inválido';
            status.classList.add('text-red-600');
            return;
        }

        const data = await resp.json(); 

        if (!data.token) {
            status.textContent = 'Login OK, mas não recebi o token do backend.';
            status.classList.add('text-red-600');
            return;
        }

        auth.token = data.token;
        auth.nome = data.nomeUsuario; 

        localStorage.setItem(AUTH_KEY, JSON.stringify(auth));

        status.textContent = 'Login bem-sucedido!';
        status.classList.add('text-green-600');
        
        atualizarInterface();
        listarLeituras(); 

    } catch (e) {
        console.error("Erro no login:", e);
        status.textContent = 'Erro ao conectar com o backend.';
        status.classList.add('text-red-600');
    }
}

function logout() {
    auth = { token: null, nome: null };
    localStorage.removeItem(AUTH_KEY);
    atualizarInterface();
    document.querySelector('#tabelaLeituras tbody').innerHTML = '';
    document.getElementById('leiturasStatus').textContent = '';
    cancelarEdicao(); 
}

async function listarLeituras() {
    if (!auth.token) {
        document.getElementById('leiturasStatus').textContent = 'Faça login primeiro.';
        document.getElementById('leiturasStatus').classList.add('text-red-600');
        return;
    }

    const status = document.getElementById('leiturasStatus');
    status.textContent = 'Carregando leituras...';
    status.classList.remove('text-red-600');

    try {
        const resp = await fetch(API_BASE + '/api/leituras', {
            method: 'GET',
            headers: { 'Authorization': 'Bearer ' + auth.token }
        });

        if (resp.status === 401 || resp.status === 403) {
             status.textContent = 'Sessão expirada. Faça login novamente.';
             status.classList.add('text-red-600');
             logout(); 
             return;
        }
        if (!resp.ok) {
            status.textContent = 'Erro ao buscar leituras (HTTP ' + resp.status + ').';
            status.classList.add('text-red-600');
            return;
        }

        const leituras = await resp.json();
        const tbody = document.querySelector('#tabelaLeituras tbody');
        tbody.innerHTML = ''; 

        if (leituras.length === 0) {
            status.textContent = 'Nenhuma leitura encontrada.';
            return;
        }

        leituras.forEach(l => {
            const tr = document.createElement('tr');
            tr.className = 'hover:bg-gray-50';
            
            const numSerie = l.hidrometro ? l.hidrometro.numSerieHidrometro : 'N/D';
            const dataFormatada = new Date(l.dataHoraLeitura).toLocaleString('pt-BR');
            const valorFormatado = l.valorMedido.toFixed(3);

        
            tr.innerHTML = `
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">${l.codLeitura}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">${numSerie}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">${valorFormatado}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">${dataFormatada}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button 
                        onclick="prepararEdicao('${l.codLeitura}', '${numSerie}', ${l.valorMedido})" 
                        class="text-blue-600 hover:text-blue-900 mr-3">
                        Editar
                    </button>
                    <button 
                        onclick="deletarLeitura('${l.codLeitura}')" 
                        class="text-red-600 hover:text-red-900">
                        Excluir
                    </button>
                </td>
            `;
       
            tbody.appendChild(tr);
        });

        status.textContent = 'Total de leituras: ' + leituras.length;
    } catch (e) {
        console.error("Erro ao listar leituras:", e);
        status.textContent = 'Erro ao conectar com o backend.';
        status.classList.add('text-red-600');
    }
}


async function salvarLeitura() {
    if (!auth.token) {
        document.getElementById('leiturasStatus').textContent = 'Faça login primeiro.';
        document.getElementById('leiturasStatus').classList.add('text-red-600');
        return;
    }

    const status = document.getElementById('leiturasStatus');
    const valorMedido = parseFloat(document.getElementById('valorMedido').value);

   
    if (isNaN(valorMedido)) {
        status.textContent = 'O Valor Medido é inválido.';
        status.classList.add('text-red-600');
        return;
    }

    let url = `${API_BASE}/api/leituras`;
    let method = 'POST';
    let body = {};

    if (modoEdicao.ativo) {
      
        url = `${API_BASE}/api/leituras/${modoEdicao.id}`;
        method = 'PUT';
        body = {
            valorMedido: valorMedido
            
        };
        status.textContent = `Salvando alterações na leitura ${modoEdicao.id}...`;
    } else {
    
        const numSerie = document.getElementById('numSerie').value;
        if (!numSerie) {
            status.textContent = 'Preencha o Número de Série do Hidrômetro.';
            status.classList.add('text-red-600');
            return;
        }
        body = {
            numSerieHidrometro: numSerie,
            valorMedido: valorMedido
        };
        status.textContent = 'Enviando leitura...';
    }

    status.classList.remove('text-red-600');

    try {
        const resp = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + auth.token
            },
            body: JSON.stringify(body)
        });

        if (resp.status === 401 || resp.status === 403) {
             status.textContent = 'Sessão expirada. Faça login novamente.';
             status.classList.add('text-red-600');
             logout(); 
             return;
        }
        if (!resp.ok) {
            const txt = await resp.text();
            status.textContent = `Erro ao salvar: ${txt}`;
            status.classList.add('text-red-600');
            return;
        }

        status.textContent = 'Leitura salva com sucesso.';
        status.classList.remove('text-red-600');
        status.classList.add('text-green-600');
        
        if (modoEdicao.ativo) {
            cancelarEdicao();
        } else {
            
            document.getElementById('numSerie').value = '';
            document.getElementById('valorMedido').value = '';
        }
        
        listarLeituras(); 
    } catch (e) {
        console.error("Erro ao salvar leitura:", e);
        status.textContent = 'Erro ao conectar com o backend.';
        status.classList.add('text-red-600');
    }
}


async function deletarLeitura(id) {
    if (!auth.token) {
        alert('Sessão expirada. Faça login novamente.');
        logout();
        return;
    }

    if (!confirm(`Tem certeza que deseja excluir a leitura ID ${id}?`)) {
        return;
    }

    const status = document.getElementById('leiturasStatus');
    status.textContent = `Excluindo leitura ${id}...`;
    status.classList.remove('text-red-600');

    try {
        const resp = await fetch(`${API_BASE}/api/leituras/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': 'Bearer ' + auth.token
            }
        });

        if (resp.status === 401 || resp.status === 403) {
             status.textContent = 'Sessão expirada. Faça login novamente.';
             status.classList.add('text-red-600');
             logout();
             return;
        }

        if (!resp.ok && resp.status !== 204) { 
             status.textContent = 'Erro ao excluir leitura (HTTP ' + resp.status + ').';
             status.classList.add('text-red-600');
            return;
        }

        status.textContent = `Leitura ${id} excluída com sucesso.`;
        status.classList.add('text-green-600');
        
        listarLeituras();

    } catch (e) {
        console.error("Erro ao excluir leitura:", e);
        status.textContent = 'Erro ao conectar com o backend.';
        status.classList.add('text-red-600');
    }
}


function prepararEdicao(id, numSerie, valorMedido) {
    modoEdicao.ativo = true;
    modoEdicao.id = id;

    document.getElementById('numSerie').value = numSerie;
    document.getElementById('valorMedido').value = valorMedido;
    document.getElementById('numSerie').disabled = true;

    const btnCriar = document.getElementById('btnCriar');
    btnCriar.textContent = 'Salvar Alterações';
    btnCriar.classList.remove('bg-green-600', 'hover:bg-green-700');
    btnCriar.classList.add('bg-blue-600', 'hover:bg-blue-700');

    if (!document.getElementById('btnCancelar')) {
        const btnCancelar = document.createElement('button');
        btnCancelar.id = 'btnCancelar';
        btnCancelar.textContent = 'Cancelar';
        btnCancelar.className = `w-full bg-gray-500 text-white font-semibold py-3 px-6 rounded-lg
                                hover:bg-gray-600 focus:outline-none focus:ring-2 
                                focus:ring-gray-500 focus:ring-offset-2 transition-colors 
                                duration-200`;
        btnCancelar.onclick = cancelarEdicao; 
        
        const gridContainer = btnCriar.parentElement;
        gridContainer.classList.remove('md:grid-cols-3');
        gridContainer.classList.add('md:grid-cols-4');
        
        btnCriar.classList.remove('md:col-span-1');
        btnCriar.classList.add('md:col-start-3'); 
        
        gridContainer.appendChild(btnCancelar); 
    }

    window.scrollTo(0, 0);
    document.getElementById('valorMedido').focus();
}

function cancelarEdicao() {

    modoEdicao.ativo = false;
    modoEdicao.id = null;

    document.getElementById('numSerie').value = '';
    document.getElementById('valorMedido').value = '';
    document.getElementById('numSerie').disabled = false;

    const btnCriar = document.getElementById('btnCriar');
    btnCriar.textContent = 'Criar Leitura';
    btnCriar.classList.remove('bg-blue-600', 'hover:bg-blue-700');
    btnCriar.classList.add('bg-green-600', 'hover:bg-green-700');
 
    const btnCancelar = document.getElementById('btnCancelar');
    if (btnCancelar) {
        const gridContainer = btnCancelar.parentElement;
        gridContainer.removeChild(btnCancelar);
        
        gridContainer.classList.remove('md:grid-cols-4');
        gridContainer.classList.add('md:grid-cols-3');
        
        btnCriar.classList.remove('md:col-start-3');
    }
}


function atualizarInterface() {
    const loginCard = document.getElementById('login-card');
    const leiturasCard = document.getElementById('leituras-card');
    const loginStatus = document.getElementById('loginStatus');

    if (auth.token) {
        loginCard.style.display = 'none';
        leiturasCard.style.display = 'block';
        loginStatus.textContent = ''; 
        
        const h2 = leiturasCard.querySelector('h2');
        if (!document.getElementById('btnLogout')) {
            h2.innerHTML = `Gerenciar Leituras <span class="text-base font-medium text-gray-600">(Logado: ${auth.nome})</span>`;
            
            const btnLogout = document.createElement('button');
            btnLogout.id = 'btnLogout';
            btnLogout.textContent = 'Sair (Logout)';
            btnLogout.className = `w-full md:w-auto bg-red-600 text-white font-semibold py-2 px-5 rounded-lg
                                    hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 
                                    focus:ring-offset-2 transition-colors duration-200 mb-4 ml-0 md:ml-4`; 
            btnLogout.addEventListener('click', logout);
            
            document.getElementById('btnListar').insertAdjacentElement('afterend', btnLogout);
        }

    } else {
        loginCard.style.display = 'block';
        leiturasCard.style.display = 'none';
        
        const btnLogout = document.getElementById('btnLogout');
        if (btnLogout) {
            btnLogout.remove();
        }
        leiturasCard.querySelector('h2').innerHTML = 'Gerenciar Leituras';
    }
}

function init() {
    const dadosSalvos = localStorage.getItem(AUTH_KEY);
    if (dadosSalvos) {
        auth = JSON.parse(dadosSalvos);
    }
    
    atualizarInterface();
    
    if (auth.token) {
        listarLeituras();
    }
    
    document.getElementById('btnLogin').addEventListener('click', login);
    document.getElementById('btnListar').addEventListener('click', listarLeituras);
    document.getElementById('btnCriar').addEventListener('click', salvarLeitura);
}

document.addEventListener('DOMContentLoaded', init);