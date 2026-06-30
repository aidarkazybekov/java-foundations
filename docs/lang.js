(function () {
  function current() {
    return document.documentElement.getAttribute('data-lang') || 'en';
  }
  function apply(lang) {
    document.documentElement.setAttribute('data-lang', lang);
    document.documentElement.setAttribute('lang', lang);
    try { localStorage.setItem('jf-lang', lang); } catch (e) {}
    var dict = (window.I18N && window.I18N[lang]) || {};
    document.querySelectorAll('[data-i18n]').forEach(function (el) {
      var v = dict[el.getAttribute('data-i18n')];
      if (v != null) el.textContent = v;
    });
    var b = document.getElementById('lang-toggle');
    if (b) b.textContent = lang === 'ru' ? 'EN' : 'RU';
    if (typeof window.onLangChange === 'function') window.onLangChange(lang);
  }
  window.toggleLang = function () { apply(current() === 'ru' ? 'en' : 'ru'); };
  window.applyLang = apply;
  function init() {
    var saved;
    try { saved = localStorage.getItem('jf-lang'); } catch (e) {}
    apply(saved || current());
  }
  if (document.readyState !== 'loading') init();
  else document.addEventListener('DOMContentLoaded', init);
})();
