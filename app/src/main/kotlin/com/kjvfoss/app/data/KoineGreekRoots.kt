package com.kjvfoss.app.data

data class GreekRoot(
  override val englishTerm: String,
  val greek: String,
  override val transliteration: String,
  override val literalMeaning: String,
  val matchedWords: Set<String>,
  val matchedPhrases: Set<String> = emptySet(),
) : StudyRoot {
  override val originalScript: String get() = greek
}

private val greekRoots = listOf(
  GreekRoot("God", "θεός", "theos", "God; a deity", setOf("god")),
  GreekRoot("Lord", "κύριος", "kyrios", "lord; master", setOf("lord", "lords", "lord's")),
  GreekRoot("Jesus", "Ἰησοῦς", "Iēsous", "Jesus", setOf("jesus")),
  GreekRoot("Christ", "Χριστός", "Christos", "anointed one; Christ", setOf("christ")),
  GreekRoot("Spirit", "πνεῦμα", "pneuma", "spirit; wind; breath", setOf("spirit", "spirits", "ghost")),
  GreekRoot("word", "λόγος", "logos", "word; message; account", setOf("word", "words")),
  GreekRoot("saying", "ῥῆμα", "rhēma", "spoken word; saying", setOf("saying", "sayings")),
  GreekRoot("love", "ἀγάπη", "agapē", "love; goodwill", setOf("love", "charity")),
  GreekRoot("love", "ἀγαπάω", "agapaō", "to love; to cherish", setOf("loved", "loveth", "loving")),
  GreekRoot("grace", "χάρις", "charis", "grace; favor; gift", setOf("grace")),
  GreekRoot("faith", "πίστις", "pistis", "faith; trust; faithfulness", setOf("faith", "faithful")),
  GreekRoot("believe", "πιστεύω", "pisteuō", "to believe; to trust", setOf("believe", "believed", "believeth")),
  GreekRoot("repentance", "μετάνοια", "metanoia", "repentance; change of mind", setOf("repentance")),
  GreekRoot("repent", "μετανοέω", "metanoeō", "to repent; to change one's mind", setOf("repent", "repented", "repenteth")),
  GreekRoot("flesh", "σάρξ", "sarx", "flesh; human nature", setOf("flesh")),
  GreekRoot(
    "born again",
    "γεννηθῇ ἄνωθεν",
    "gennēthē anōthen",
    "be born from above; be born again",
    emptySet(),
    setOf("born again", "born from above"),
  ),
  GreekRoot("human", "ἄνθρωπος", "anthrōpos", "human being; person", setOf("man", "men", "mankind", "human")),
  GreekRoot("life", "ζωή", "zōē", "life", setOf("life", "lives", "living")),
  GreekRoot("death", "θάνατος", "thanatos", "death", setOf("death", "dead", "died")),
  GreekRoot("world", "κόσμος", "kosmos", "world; ordered creation", setOf("world", "worlds")),
  GreekRoot("church", "ἐκκλησία", "ekklēsia", "assembly; congregation", setOf("church", "churches")),
  GreekRoot("kingdom", "βασιλεία", "basileia", "kingdom; reign", setOf("kingdom", "kingdoms")),
  GreekRoot("gospel", "εὐαγγέλιον", "euangelion", "good news; gospel", setOf("gospel", "gospels")),
  GreekRoot("truth", "ἀλήθεια", "alētheia", "truth; reality", setOf("truth")),
  GreekRoot("glory", "δόξα", "doxa", "glory; honor; reputation", setOf("glory", "glorious")),
  GreekRoot("power", "δύναμις", "dynamis", "power; ability; mighty work", setOf("power", "powers")),
  GreekRoot("salvation", "σωτηρία", "sōtēria", "salvation; deliverance", setOf("salvation")),
  GreekRoot("hope", "ἐλπίς", "elpis", "hope; expectation", setOf("hope")),
  GreekRoot("peace", "εἰρήνη", "eirēnē", "peace; wellbeing", setOf("peace")),
  GreekRoot("good", "ἀγαθός", "agathos", "good; beneficial", setOf("good", "goodness")),
  GreekRoot("evil", "πονηρός", "ponēros", "evil; wicked", setOf("evil", "wicked")),
  GreekRoot("light", "φῶς", "phōs", "light", setOf("light")),
  GreekRoot("darkness", "σκοτία", "skotia", "darkness", setOf("darkness")),
  GreekRoot("name", "ὄνομα", "onoma", "name; reputation", setOf("name", "names")),
  GreekRoot("blood", "αἷμα", "haima", "blood", setOf("blood")),
  GreekRoot("water", "ὕδωρ", "hydōr", "water", setOf("water", "waters")),
  GreekRoot("bread", "ἄρτος", "artos", "bread; loaf", setOf("bread", "loaves")),
  GreekRoot("resurrection", "ἀνάστασις", "anastasis", "raising; resurrection", setOf("resurrection")),
  GreekRoot("mercy", "ἔλεος", "eleos", "mercy; compassion", setOf("mercy")),
  GreekRoot("holy", "ἅγιος", "hagios", "holy; set apart", setOf("holy", "holiness")),
  GreekRoot("righteousness", "δικαιοσύνη", "dikaiosynē", "righteousness; justice", setOf("righteousness")),
  GreekRoot("cross", "σταυρός", "stauros", "cross; stake", setOf("cross", "crosses")),
  GreekRoot("witness", "μάρτυς", "martys", "witness; one who testifies", setOf("witness", "witnesses")),
  GreekRoot("judge", "κρίνω", "krinō", "to judge; to decide", setOf("judge", "judged", "judgeth")),
  GreekRoot("judgment", "κρίσις", "krisis", "judgment; decision", setOf("judgment")),
  GreekRoot("angel", "ἄγγελος", "angelos", "messenger; angel", setOf("angel", "angels")),
  GreekRoot("prophet", "προφήτης", "prophētēs", "prophet; one who speaks forth", setOf("prophet", "prophets")),
  GreekRoot("disciple", "μαθητής", "mathētēs", "disciple; learner", setOf("disciple", "disciples")),
  GreekRoot("apostle", "ἀπόστολος", "apostolos", "one sent; apostle", setOf("apostle", "apostles")),
  GreekRoot("king", "βασιλεύς", "basileus", "king", setOf("king", "kings")),
  GreekRoot("child", "τέκνον", "teknon", "child; descendant", setOf("child", "children")),
  GreekRoot("prayer", "προσευχή", "proseuchē", "prayer", setOf("prayer", "prayers")),
  GreekRoot("pray", "προσεύχομαι", "proseuchomai", "to pray", setOf("pray", "prayed", "prayeth")),
  GreekRoot("baptize", "βαπτίζω", "baptizō", "to immerse; to baptize", setOf("baptize", "baptized", "baptizeth")),
  GreekRoot("baptism", "βάπτισμα", "baptisma", "baptism; immersion", setOf("baptism")),
  GreekRoot("body", "σῶμα", "sōma", "body", setOf("body", "bodies")),
  GreekRoot("mind", "νοῦς", "nous", "mind; understanding", setOf("mind", "minds")),
  GreekRoot("soul", "ψυχή", "psychē", "soul; life", setOf("soul", "souls")),
  GreekRoot("wisdom", "σοφία", "sophia", "wisdom", setOf("wisdom")),
  GreekRoot("gift", "χάρισμα", "charisma", "gracious gift", setOf("gift", "gifts")),
  GreekRoot("serve", "διακονέω", "diakoneō", "to serve; to minister", setOf("serve", "served", "serving")),
  GreekRoot("hear", "ἀκούω", "akouō", "to hear; to listen", setOf("hear", "heard", "heareth")),
  GreekRoot("call", "καλέω", "kaleō", "to call; to name", setOf("call", "called", "calleth")),
  GreekRoot("commandment", "ἐντολή", "entolē", "command; instruction", setOf("commandment", "commandments")),
  GreekRoot("devil", "διάβολος", "diabolos", "slanderer; devil", setOf("devil")),
  GreekRoot("Satan", "σατανᾶς", "satanas", "adversary; Satan", setOf("satan")),
  GreekRoot("father", "πατήρ", "patēr", "father", setOf("father", "fathers")),
  GreekRoot("son", "υἱός", "huios", "son", setOf("son", "sons")),
  GreekRoot("brother", "ἀδελφός", "adelphos", "brother; fellow believer", setOf("brother", "brothers", "brethren")),
  GreekRoot("covenant", "διαθήκη", "diathēkē", "covenant; testament", setOf("covenant", "covenants", "testament")),
  GreekRoot("law", "νόμος", "nomos", "law; custom", setOf("law", "laws")),
  GreekRoot("promise", "ἐπαγγελία", "epangelia", "promise", setOf("promise", "promises", "promised")),
  GreekRoot("work", "ἔργον", "ergon", "work; deed", setOf("work", "works", "worked")),
  GreekRoot("forgive", "ἀφίημι", "aphiēmi", "to release; to forgive", setOf("forgive", "forgiven", "forgave")),
  GreekRoot("forgiveness", "ἄφεσις", "aphesis", "release; forgiveness", setOf("forgiveness")),
  GreekRoot("joy", "χαρά", "chara", "joy; gladness", setOf("joy")),
  GreekRoot("servant", "δοῦλος", "doulos", "servant; enslaved person", setOf("servant", "servants")),
  GreekRoot("house", "οἶκος", "oikos", "house; household", setOf("house", "houses")),
  GreekRoot("heaven", "οὐρανός", "ouranos", "heaven; sky", setOf("heaven", "heavens")),
  GreekRoot("seed", "σπέρμα", "sperma", "seed; offspring", setOf("seed", "seeds")),
  GreekRoot("temple", "ναός", "naos", "temple; sanctuary", setOf("temple", "temples")),
  GreekRoot("beginning", "ἀρχή", "archē", "beginning; rule", setOf("beginning")),
  GreekRoot("Holy Ghost", "παράκλητος", "paraklētos", "advocate; helper", setOf("comforter", "advocate")),
  GreekRoot("Godhead", "θειότης", "theiotēs", "divinity; divine nature", setOf("godhead")),
  GreekRoot("message", "εὐαγγελίζω", "euangelizō", "to announce good news", setOf("preach", "preached", "preacheth")),
  GreekRoot("service", "διακονία", "diakonia", "service; ministry", setOf("service", "ministry")),
  GreekRoot("joyful", "χαίρω", "chairō", "to rejoice; be glad", setOf("rejoice", "rejoiced", "rejoiceth")),
  GreekRoot("obedience", "ὑπακοή", "hypakoē", "obedience; compliance", setOf("obedience")),
  GreekRoot("freedom", "ἐλευθερία", "eleutheria", "freedom; liberty", setOf("liberty", "freedom")),
  GreekRoot("adoption", "υἱοθεσία", "huiothesia", "adoption; placing as a son", setOf("adoption")),
  GreekRoot("condemnation", "κατάκριμα", "katakrima", "condemnation; adverse judgment", setOf("condemnation")),
  GreekRoot("sanctification", "ἁγιασμός", "hagiasmos", "holiness; consecration", setOf("sanctification")),
)

fun findGreekRoots(chapter: BibleChapter): List<GreekRoot> {
  if (isOldTestamentChapter(chapter)) return emptyList()
  val normalizedText = chapter.verses.joinToString(" ") { it.text.lowercase() }
  val chapterWords = Regex("[a-z']+")
    .findAll(normalizedText)
    .map { it.value.trim('\'') }
    .toSet()
  val phraseText = " ${normalizedText.replace(Regex("[^a-z]+"), " ").trim()} "
  val matches = greekRoots.filter { root ->
    root.matchedWords.any(chapterWords::contains) ||
      root.matchedPhrases.any { phrase -> phraseText.contains(" $phrase ") }
  }
  val phraseMatches = matches.filter { it.matchedPhrases.isNotEmpty() }
  return (phraseMatches + matches.filterNot { it in phraseMatches }).take(12)
}
