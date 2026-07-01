// Shared card rendering for the landing, Play and Learn pages.
// Auto-generates a colored cover per item (hue derived from the title) so cards are
// visual, distinct, and work for future contributions with no manual assets.
(function () {
  function hue(s) {
    var h = 0;
    for (var i = 0; i < s.length; i++) h = (h * 31 + s.charCodeAt(i)) >>> 0;
    return h % 360;
  }
  function esc(s) {
    return String(s).replace(/[&<>"]/g, function (c) {
      return { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;' }[c];
    });
  }
  function cover(title, kind) {
    var h = hue(title);
    var bg = 'linear-gradient(135deg, hsl(' + h + ' 68% 56%), hsl(' + ((h + 45) % 360) + ' 68% 46%))';
    var glyph = kind === 'viz' ? '▶' : '&lt;/&gt;';
    return '<div class="cover" style="background:' + bg + '"><span class="cover-glyph">' + glyph + '</span></div>';
  }
  window.jfCardHTML = function (item, kind, lang) {
    var href = kind === 'viz' ? ('visualizations/' + item.file) : ('concept.html?doc=' + item.slug);
    var meta = kind === 'viz'
      ? (lang === 'ru' ? 'запустить →' : 'play →')
      : (lang === 'ru' ? 'читать →' : 'read →');
    return '<a class="card" href="' + href + '">' + cover(item.title, kind)
      + '<div class="card-body"><h3>' + esc(item.title) + '</h3><p>' + esc(item.desc)
      + '</p><span class="meta">' + meta + '</span></div></a>';
  };
  window.jfLang = function () { return document.documentElement.getAttribute('data-lang') || 'en'; };
  window.jfLoad = function (cb) {
    fetch('content.json').then(function (r) { return r.json(); }).then(cb).catch(function () {});
  };
})();
