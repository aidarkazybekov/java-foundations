// Auto-generates content.json from the files in docs/concepts and docs/visualizations,
// so contributed concepts/visualizations appear on the site automatically.
// Run: node docs/gen-manifest.mjs   (also run by the GitHub Pages workflow)
import { readdirSync, readFileSync, writeFileSync, existsSync } from 'node:fs';
import { dirname, join } from 'node:path';
import { fileURLToPath } from 'node:url';

const DOCS = dirname(fileURLToPath(import.meta.url));

function stripTags(s) {
  return s.replace(/<[^>]+>/g, '').replace(/\s+/g, ' ').trim();
}

function clip(s, max = 150) {
  if (s.length <= max) return s;
  const cut = s.slice(0, max);
  const sp = cut.lastIndexOf(' ');
  return (sp > 60 ? cut.slice(0, sp) : cut).replace(/[\s.,—-]+$/, '') + '…';
}

// --- concepts: bilingual. EN = concepts/<slug>.md, RU = concepts/ru/<slug>.md ---
// title = first "# ...", desc = first "> ..." blockquote.
const conceptsDir = join(DOCS, 'concepts');
const ruDir = join(conceptsDir, 'ru');
function extractMeta(md, fallback) {
  const title = (md.match(/^#\s+(.+)$/m) || [, fallback])[1].trim();
  const desc = clip(stripTags((md.match(/^>\s+(.+)$/m) || [, ''])[1]));
  return { title, desc };
}
const concepts = readdirSync(conceptsDir)
  .filter(f => f.endsWith('.md') && f.toLowerCase() !== 'readme.md')
  .map(f => {
    const slug = f.replace(/\.md$/, '');
    const en = extractMeta(readFileSync(join(conceptsDir, f), 'utf8'), slug);
    const ruPath = join(ruDir, f);
    const ru = existsSync(ruPath) ? extractMeta(readFileSync(ruPath, 'utf8'), slug) : en;
    return { slug, title: { en: en.title, ru: ru.title }, desc: { en: en.desc, ru: ru.desc } };
  })
  .sort((a, b) => a.title.en.localeCompare(b.title.en));

// --- visualizations: title = <h1>, desc = <p class="lead"> ---
const vizDir = join(DOCS, 'visualizations');
const visualizations = readdirSync(vizDir)
  .filter(f => f.endsWith('.html') && f.toLowerCase() !== 'index.html' && f.toLowerCase() !== 'readme.md')
  .map(f => {
    const html = readFileSync(join(vizDir, f), 'utf8');
    const h1 = html.match(/<h1[^>]*>([\s\S]*?)<\/h1>/i);
    const titleSrc = h1 ? h1[1] : (html.match(/<title>([\s\S]*?)<\/title>/i) || [, f])[1];
    const lead = html.match(/<p class="lead"[^>]*>([\s\S]*?)<\/p>/i);
    return {
      file: f,
      title: stripTags(titleSrc).replace(/ — java-foundations$/, ''),
      desc: lead ? clip(stripTags(lead[1])) : '',
    };
  })
  .sort((a, b) => a.title.localeCompare(b.title));

const out = { generated: true, concepts, visualizations };
writeFileSync(join(DOCS, 'content.json'), JSON.stringify(out, null, 2) + '\n');
console.log(`content.json: ${concepts.length} concepts, ${visualizations.length} visualizations`);
