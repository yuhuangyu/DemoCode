package com.api.AccessibilityService;

import java.util.HashMap;

/**
 * Created by ASUS on 2017/12/5.
 */

public class GPWordsUtils {
    private static String KEY_COUNTRY = "values-ca,values-sq,values-kk,values-sr,values-km,values-ko,values-sw,values-kn,values-sv,values-ka,values-si,values-sk,values-sl,values-gu,values-ky,values-fr-rCA,values-ta,values-cs,values-pa,values-te,values-th,values-zh-rCN,values-gl,values-nl,values-ro,values-en-rIN,values-be,values-fi,values-ru,values-ja,values-ne,values-az,values-fa,values-zu,values-bg,values-bn,values-fr,values-zh-rHK,values-zh-rTW,values-mk,values-ur,values-ml,values-mn,values,values-uk,values-id,values-pt-rPT,values-en-rAU,values-es,values-iw,values-eu,values-et,values-ar,values-nb,values-vi,values-es-rUS,values-ms,values-af,values-mr,values-uz,values-el,values-is,values-am,values-my,values-it,values-tr,values-da,values-de,values-pt,values-lo,values-en-rGB,values-tl,values-pl,values-hu,values-hy,values-lt,values-hr,values-lv";
    private  static String ACTION_INSTALL = "Instal·la,Instalo,Орнату,Инсталирај,ដំឡើង,설치,Sakinisha,ಸ್ಥಾಪಿಸು,Installera,ინსტალაცია,ස්ථාපනය කරන්න,Inštalovať,Namesti,ઇન્સ્ટોલ કરો,Орнотуу,Installer,நிறுவு,Instalovat,ਸਥਾਪਤ ਕਰੋ,ఇన్\u200Cస్టాల్ చేయి,ติดตั้ง,安装,Instalar,Installeren,Instalați,Install,Усталяваць,Asenna,Установить,インストール,स्थापना गर्नुहोस्,Quraşdırın,نصب,Faka,Инсталиране,ইনস্টল করুন,Installer,安裝,安裝,Инсталирај,انسٹال کریں,ഇന്\u200Dസ്റ്റാള്\u200D ചെയ്യുക,Суулгах,Install,Установити,Pasang,Instalar,Install,Instalar,התקן,Instalatu,Installi,تثبيت,Installer,Cài đặt,Instalar,Pasang,Installeer,स्\u200Dथापित करा,O‘rnatish,Εγκατάσταση,Setja upp,ጫን,သွင်းရန်,Installa,Yükle,Installer,Installieren,Instalar,ຕິດຕັ້ງ,Install,I-install,Zainstaluj,Telepítés,Տեղադրել,Įdiegti,Instaliraj,Instalēt";
    private  static String ACTION_ACCEPT = "Accepta,Prano,Қабылдау,Прихватам,ព្រម\u200Bទទួល,수락,Kubali,ಸಮ್ಮತಿಸು,Tacka ja,დათანხმება,පිළිගන්න,Prijať,Sprejmem,સ્વીકારો,Кабыл алуу,Accepter,அனுமதி,Přijmout,ਸਵੀਕਾਰ ਕਰੋ,ఆమోదిస్తున్నాను,ยอมรับ,接受,Aceptar,Accepteren,Accept,Accept,Прыняць,Hyväksy,Принять,同意する,स्वीकार्नुहोस्,Qəbul edin,پذیرش,Yamukela,Приемам,স্বীকার,Accepter,接受,接受,Прифати,قبول کریں,അംഗീകരിക്കുക,Зөвшөөрөх,Accept,Приймаю,Terima,Aceitar,Accept,Aceptar,קבל,Onartu,Nõustu,قبول,Godta,Chấp nhận,Aceptar,Terima,Aanvaar,स्वीकार करा,Roziman,Αποδοχή,Samþykkja,ተቀበል,အိုကေ,Accetto,Kabul et,Acceptér,Annehmen,Aceitar,ຍອມ\u200Bຮັບ,Accept,Tanggapin,Zaakceptuj,Elfogadás,Ընդունել,Sutikti,Prihvati,Piekrist";
    private  static String ACTION_OPEN = "Obre,Hap,Ашу,Отвори,បើក,열기,Fungua,ತೆರೆ,Öppna,გახსნა,විවෘත කරන්න,Otvoriť,Odpri,ખુલ્લું,Ачуу,Ouvrir,திற,Otevřít,ਖੋਲ੍ਹੋ,తెరువు,เปิด,打开,Abrir,Openen,Deschideți,Open,Адкрыць,Avaa,Открыть,開く,खोल्नुहोस्,Açın,باز شود,Vula,Отваряне,খুলুন,Ouvrir,開啟,開啟,Отвори,کھولیں,തുറക്കുക,Нээх,Open,Відкрити,Buka,Abrir,Open,Abrir,פתח,Ireki,Ava,فتح,Åpne,Mở,Abrir,Buka,Maak oop,उघडा,Ochish,Άνοιγμα,Opna,ክፈት,ဖွင့်ရန်,Apri,Aç,Åbn,Öffnen,Abrir,ເປີດ,Open,Buksan,Otwórz,Megnyitás,Բացել,Atidaryti,Otvori,Atvērt";
    private static HashMap<String, String> mapInstall;
    private static HashMap<String, String> mapAccept;
    private static HashMap<String, String> mapOpen;
    private static GPWordsUtils _gpUtils = null;

    private GPWordsUtils(){

    }
    public static GPWordsUtils getInstance(){
        if(_gpUtils == null){
            synchronized (GPWordsUtils.class){
                if(_gpUtils == null){
                    String[] strings = diposeListTag(KEY_COUNTRY);
                    String[] strings1 = diposeListTag(ACTION_INSTALL);
                    String[] strings2 = diposeListTag(ACTION_ACCEPT);
                    String[] strings3 = diposeListTag(ACTION_OPEN);

                    mapInstall = new HashMap<>();
                    mapAccept = new HashMap<>();
                    mapOpen = new HashMap<>();

                    if (strings != null && strings1 != null && strings2 != null && strings3 != null &&
                            strings.length==78 && strings1.length==78 && strings2.length==78 && strings3.length==78) {
                        for (int i = 0; i < strings.length; i++) {
                            mapInstall.put(strings[i],strings1[i]);
                            mapAccept.put(strings[i],strings2[i]);
                            mapOpen.put(strings[i],strings3[i]);
                        }
                    }
                    _gpUtils = new GPWordsUtils();
                }
            }
        }
        return _gpUtils;
    }
    public HashMap getMapInstall(){
        return mapInstall;
    }
    public HashMap getMapAccept(){
        return mapAccept;
    }
    public HashMap getMapOpen(){
        return mapOpen;
    }

    private static String[] diposeListTag(String tagList) {
        if (tagList != null && tagList.length() != 0) {
            String[] tags = tagList.split(",");
            if (tags != null && tags.length != 0) {
                return tags;
            }
        }
        return null;
    }
}
