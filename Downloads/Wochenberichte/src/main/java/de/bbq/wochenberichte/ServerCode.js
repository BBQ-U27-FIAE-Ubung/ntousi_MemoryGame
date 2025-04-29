//Servercode für Cloudflare Workers

export default {
  async fetch(request, env, ctx) {
    const anfrageText = await request.text();
 
    // HTTP-Anfrage an OpenAI
    const url = "https://api.openai.com/v1/responses";
    const token = env.openai;
 
    const date = new Date().toISOString();
    
    const openAIAnfrage = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer " + token,
      },
      body: JSON.stringify({
        model: "gpt-4.1",
        input: [
          {
            "role": "developer",
            "content": `
              Für Context, heute ist folgendes Datum: ${date}.
 
              Du schreibst unseren IHK Wochenbericht in XML. Der Nutzer gibt dir seine Informationen über die Woche 
              und du formulierst sie Stichpunktartig in kurzen Sätzen, die auf die Wochentage aufgegliedert sind. 
              Wir haben 25 'eingabeFelder'. Jeder Wochentag von Montag bis Freitag, hat 5 zugewiesene eingabeFelder 
              (Zeilen) nacheinander. Maximal 50 Zeichen pro Zeile. Du darfst Zeilen leer lassen und solltest den 
              vollen Platz möglichst ausfüllen, bevor du in eine andere Zeile wechselst. Halte dich kurz. 
              Gesetzliche Feiertag in Deutschland, trägst du als 'Feiertag' ein. Die Felder "stundenFelder" 
              gibt es fünf mal, für jeden Wochentag eines. An einem normalen Tag wird "9" eingetragen und an 
              Feiertagen "0". Du gibst uns nur die genannten Felder zurück und nur den reinen XML Inhalt.
              Die Vorlage wird automatisch die Wochentage / Datumsangaben ergänzen.
              
              Orientiere dich an dieser Vorlage:
 
              <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
              <wochenbericht>
                <eingabeFelder>Satz 1</eingabeFelder>
                <eingabeFelder>Satz 2</eingabeFelder>
                ...
                <stundenFelder>9</stundenFelder>
                <stundenFelder>9</stundenFelder>
                ...
              </wochenbericht>
            `,
          },
          {
            "role": "user",
            "content": anfrageText,
          },
        ]
      }),
    });
    const antwort = await openAIAnfrage.json();
 
    return new Response(antwort.output[0].content[0].text);
  },
};

