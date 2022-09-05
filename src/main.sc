require: answers.yaml
    var = answers
    
require: functions.js
    
require: slotfilling/slotFilling.sc
  module = sys.zb-common

require: common.js
  module = sys.zb-common

theme: /

    state: Правила
        q!: $regex</start>
        #q!: * ([включи*/запуст*/позов*] ~персонаж/start) *
        intent!: /Давай поиграем
        script:
          if ($parseTree && $parseTree.value != "help") {
            $jsapi.startSession();
            $session.character = getCharacterName($request);
          }
          
          $reactions.answer(answers.Greetings[$session.character]);
        #a: Игра больше-меньше. Загадаю число от 0 до 100, ты будешь отгадывать. Начнём?
        go!: /Правила/Согласен?

        state: Согласен?

            state: Да
                intent: /Согласие
                go!: /Игра

            state: Нет
                intent: /Несогласие
                script:
                    $reactions.answer(selectRandomArg(answers.Start));
                #a: Ну и ладно! Если передумаешь - скажи "давай поиграем"

    state: Игра
        script:
            $session.number = $jsapi.random(100) + 1;
            #$reactions.answer("Загадано {{$session.number}}");
            $reactions.transition("/Проверка");

    state: Проверка
        intent: /Число
        script:
            var num = $parseTree._Number;

            if (num == $session.number) {
                $reactions.answer("Ты выиграл! Хочешь еще раз?");
                $reactions.transition("/Правила/Согласен?");
            }
            else
                if (num < $session.number)
                    $reactions.answer(selectRandomArg(answers.Bigger));
                else $reactions.answer(selectRandomArg(answers.Smaller));

    state: NoMatch || noContext = true
        event!: noMatch
        script:
          $reactions.answer(selectRandomArg(answers.NoMatch));

