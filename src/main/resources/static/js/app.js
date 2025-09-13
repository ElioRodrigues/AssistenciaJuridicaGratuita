function toggleNav(){
  const btn = document.querySelector('.nav-toggle');
  const nav = document.getElementById('site-nav');
  const expanded = btn.getAttribute('aria-expanded') === 'true';
  btn.setAttribute('aria-expanded', String(!expanded));
  nav.classList.toggle('open');
}

// máscaras simples (opcional)
function maskCEP(v){ return v.replace(/\D/g,'').replace(/^(\d{5})(\d{0,3}).*/, '$1-$2'); }
function maskCPF(v){ return v.replace(/\D/g,'').replace(/(\d{3})(\d)/, '$1.$2')
                             .replace(/(\d{3})(\d)/, '$1.$2')
                             .replace(/(\d{3})(\d{1,2})$/, '$1-$2'); }
function maskPhone(v){ return v.replace(/\D/g,'')
  .replace(/^(\d{2})(\d)/, '($1) $2')
  .replace(/(\d{5})(\d{4}).*/, '$1-$2'); }

['cep','cpf','telefone'].forEach(id=>{
  const el = document.getElementById(id);
  if(!el) return;
  el.addEventListener('input', e=>{
    const t=e.target;
    if(id==='cep') t.value = maskCEP(t.value);
    if(id==='cpf') t.value = maskCPF(t.value);
    if(id==='telefone') t.value = maskPhone(t.value);
  });
});

// ===== Dropzone simples =====
(function(){
  const dz = document.getElementById('dropzone');
  const input = document.getElementById('anexos');
  const list = document.getElementById('fileList');
  if(!dz || !input || !list) return;

  const updateList = files => {
    list.innerHTML = '';
    [...files].forEach(f => {
      const li = document.createElement('li');
      li.textContent = `${f.name} (${Math.round(f.size/1024)} KB)`;
      list.appendChild(li);
    });
  };

  dz.addEventListener('click', () => input.click());
  dz.addEventListener('keydown', e => { if(e.key==='Enter' || e.key===' ') { e.preventDefault(); input.click(); }});
  dz.addEventListener('dragover', e => { e.preventDefault(); dz.classList.add('is-over'); });
  dz.addEventListener('dragleave', () => dz.classList.remove('is-over'));
  dz.addEventListener('drop', e => {
    e.preventDefault(); dz.classList.remove('is-over');
    input.files = e.dataTransfer.files;
    updateList(input.files);
  });
  input.addEventListener('change', () => updateList(input.files));
})();
