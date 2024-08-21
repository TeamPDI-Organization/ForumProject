--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `post_id` int(11) NOT NULL,
                            `content` text NOT NULL,
                            `createdby_id` int(11) NOT NULL,
                            `creation_date` timestamp NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `comments_posts_id_fk` (`post_id`),
                            KEY `comments_users_id_fk` (`createdby_id`),
                            CONSTRAINT `comments_posts_id_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
                            CONSTRAINT `comments_users_id_fk` FOREIGN KEY (`createdby_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES
                           (1,1,'DeFi is a game-changer! I\'ve started using some DeFi platforms, and the potential for financial freedom is incredible.',1,'2024-07-24 10:23:24'),
                           (2,1,'While DeFi offers many benefits, it\'s also important to be aware of the risks involved, like smart contract vulnerabilities.',2,'2024-07-24 10:23:24'),
                           (3,2,'Great reminder about security! I\'ve been considering getting a hardware wallet to better protect my assets......',3,'2024-08-19 12:54:21'),
                           (4,2,'Two-factor authentication has saved me a couple of times already. Never underestimate the importance of good security practices.',4,'2024-07-24 10:23:24'),
                           (5,3,'Stablecoins have made trading so much easier for me. I don\'t have to worry about sudden price swings anymore.',5,'2024-07-24 10:23:24'),
                           (6,3,'They\'re also perfect for international transactions. Low fees and fast transfer times make a big difference.',1,'2024-07-24 10:23:24'),
                           (7,4,'Staking has been a fantastic way to earn passive income. It\'s like earning interest on my crypto holdings.',2,'2024-07-24 10:23:24'),
                           (8,4,'Just started staking my Ethereum, and it\'s exciting to see those rewards come in. Highly recommend it!',3,'2024-07-24 10:23:24'),
                           (9,5,'Smart contracts are fascinating! They cut out the middleman and make transactions so much smoother.',4,'2024-07-24 10:23:24'),
                           (10,5,'The potential applications of smart contracts are endless. I\'m particularly interested in their use in real estate.',5,'2024-07-24 10:23:24'),
                           (11,6,'The last Bitcoin halving event was wild. It definitely had a huge impact on the market.',1,'2024-07-24 10:23:24'),
                           (12,6,'I always look forward to halving events. They seem to be a good time to reassess my investment strategy.',2,'2024-07-24 10:23:24'),
                           (13,7,'Mining can be really profitable if you have the right setup, but it\'s not as easy as it used to be.',3,'2024-07-24 10:23:24'),
                           (14,7,'I tried mining a few years ago but found it too energy-intensive. Still, it\'s a crucial part of the crypto ecosystem.',4,'2024-07-24 10:23:24'),
                           (15,8,'Tokenization is such a revolutionary concept. Imagine owning a fraction of a famous painting!',5,'2024-07-24 10:23:24'),
                           (16,8,'It really opens up investment opportunities to a broader audience. Excited to see where this goes.',1,'2024-07-24 10:23:24'),
                           (17,9,'Blockchain in supply chains can solve so many issues related to transparency and fraud. Great to see this tech in action.',2,'2024-07-24 10:23:24'),
                           (18,9,'I\'ve read about companies using blockchain for tracking food safety. It\'s a brilliant use case.',3,'2024-07-24 10:23:24'),
                           (19,10,'Security is my top priority when choosing an exchange. I\'ve had good experiences with Coinbase and Binance.',4,'2024-07-24 10:23:24'),
                           (20,10,'Fees can really add up. It\'s worth spending some time comparing different exchanges to find the best deal.',5,'2024-07-24 10:23:24'),
                           (21,1,'A crucial reminder: secure your cryptocurrency with strong passwords, two-factor authentication, and regular backups to protect your assets.',1,'2024-08-16 11:48:48'),
                           (24,14,'Ethereum 2.0 definitely improves scalability, but I still think other blockchains like Solana and Polkadot could give it a run for its money. It’s going to be interesting to see how this plays out.',5,'2024-08-19 08:50:21'),
                           (25,14,'The real test will be how well Ethereum 2.0 handles congestion during peak times. If it can reduce gas fees and improve transaction speed, then it’s going to be hard for other platforms to compete.',4,'2024-08-19 08:50:21'),
                           (26,15,'Stablecoins are essential, especially for those who want to hedge against the volatility of cryptos like Bitcoin and Ethereum. They’re a bridge between traditional finance and the crypto world.',3,'2024-08-19 08:50:21'),
                           (27,15,'I agree, but I’m concerned about the regulatory scrutiny stablecoins might face in the future. Governments might not be too happy about their widespread use as a substitute for fiat currencies.',1,'2024-08-19 08:50:21'),
                           (28,16,'Historically, Bitcoin has always surged after a halving event, so I wouldn’t be surprised to see a new ATH. However, the market is much more mature now, so it might not be as dramatic as previous halvings.',2,'2024-08-19 08:50:21'),
                           (29,16,'I think the market is already anticipating the halving, so a lot of the potential gains might already be priced in. But long-term, I’m still bullish on Bitcoin.',4,'2024-08-19 08:50:21'),
                           (30,17,'There’s definitely a lot of hype, but I believe certain NFTs, especially those with real utility or strong communities behind them, could hold value over the long term.',1,'2024-08-19 08:50:21'),
                           (31,17,'The market is oversaturated with low-quality NFTs right now. I think we’ll see a big correction, but the projects with real value will survive and thrive.',3,'2024-08-19 08:50:21'),
                           (32,18,'Meme coins are fun and have a strong community backing, but I don’t see them having much utility in the long run. They might stick around, but I doubt they’ll be as big in a few years.',2,'2024-08-19 08:50:21'),
                           (33,18,'It’s hard to say. If these coins can find a way to add real utility or integrate into broader ecosystems, they could have some staying power. But right now, it’s all about speculation and community hype.',3,'2024-08-19 08:50:21');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `likes`
--

DROP TABLE IF EXISTS `likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `likes` (
                         `user_id` int(11) NOT NULL,
                         `post_id` int(11) NOT NULL,
                         KEY `likes_posts_id_fk` (`post_id`),
                         KEY `likes_users_id_fk` (`user_id`),
                         CONSTRAINT `likes_posts_id_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
                         CONSTRAINT `likes_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `likes`
--

LOCK TABLES `likes` WRITE;
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
INSERT INTO `likes` VALUES
                        (1,3),
                        (1,3),
                        (3,1),
                        (3,1),
                        (3,2),
                        (3,2),
                        (3,18),
                        (3,18),
                        (3,4),
                        (3,4),
                        (2,1),
                        (2,1),
                        (2,3),
                        (2,3),
                        (2,8),
                        (2,8),
                        (2,17),
                        (2,17),
                        (1,7),
                        (1,7),
                        (1,10),
                        (1,10),
                        (1,9),
                        (1,9),
                        (1,16),
                        (1,16),
                        (4,15),
                        (4,15),
                        (4,6),
                        (4,6),
                        (4,14),
                        (4,14),
                        (4,18),
                        (4,18),
                        (5,1),
                        (5,1),
                        (3,5),
                        (3,5),
                        (8,2),
                        (8,2),
                        (9,1),
                        (9,1),
                        (10,1),
                        (10,1);
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone_numbers`
--

DROP TABLE IF EXISTS `phone_numbers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone_numbers` (
                                 `user_id` int(11) NOT NULL,
                                 `phone_number` varchar(13) NOT NULL,
                                 PRIMARY KEY (`user_id`),
                                 CONSTRAINT `phone_numbers_users_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone_numbers`
--

LOCK TABLES `phone_numbers` WRITE;
/*!40000 ALTER TABLE `phone_numbers` DISABLE KEYS */;
INSERT INTO `phone_numbers` VALUES
    (1,'0883574116');
/*!40000 ALTER TABLE `phone_numbers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `posts` (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `title` varchar(64) NOT NULL,
                         `content` text NOT NULL,
                         `createdby_id` int(11) NOT NULL,
                         `creation_date` timestamp NOT NULL,
                         PRIMARY KEY (`id`),
                         KEY `posts_users_id_fk` (`createdby_id`),
                         CONSTRAINT `posts_users_id_fk` FOREIGN KEY (`createdby_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES
                        (1,'The Rise of Decentralized Finance (DeFi)','Decentralized Finance, or DeFi, is revolutionizing the financial industry by offering decentralized alternatives to traditional banking services. Using blockchain technology, DeFi platforms enable users to lend, borrow, and trade assets without intermediaries. This new wave of financial innovation promises greater accessibility, transparency, and security, attracting both investors and tech enthusiasts. As DeFi continues to grow, it\'s reshaping the future of finance.',1,'2024-08-20 17:43:27'),
                        (2,'The Importance of Cryptocurrency Wallet Security','With the increasing popularity of cryptocurrencies, securing your digital assets has never been more critical. Cryptocurrency wallets, which store your private keys, come in various forms such as hardware, software, and paper wallets. Each type offers different levels of security and convenience. To protect your funds, it\'s essential to use strong passwords, enable two-factor authentication, and regularly back up your wallet. Staying informed about potential threats and security best practices will help ensure your cryptocurrency investments remain safe.\n\n\n\n\n\n\n',2,'2024-07-22 12:56:13'),
                        (3,'Are There Benefits of Using Stablecoins?','Stablecoins are cryptocurrencies designed to minimize price volatility by pegging their value to a stable asset, like the US dollar. They offer the stability of traditional currencies with the benefits of crypto, such as fast transactions and low fees. Ideal for trading and everyday transactions, stablecoins are becoming an essential part of the crypto ecosystem.',3,'2024-08-19 12:53:43'),
                        (4,'What is Staking in Cryptocurrency?','Staking involves holding and locking up a certain amount of cryptocurrency to support the operations of a blockchain network. In return, participants earn rewards, often in the form of additional coins. Staking not only provides passive income but also contributes to the network\'s security and efficiency.',4,'2024-07-22 14:28:53'),
                        (5,'The Role of Smart Contracts','Smart contracts are self-executing contracts with the terms directly written into code. They automatically execute transactions when predetermined conditions are met, eliminating the need for intermediaries. This technology enhances transparency and reduces costs, playing a crucial role in various blockchain applications.',5,'2024-07-22 14:28:53'),
                        (6,'Understanding Bitcoin Halving','Bitcoin halving occurs approximately every four years, reducing the reward for mining new blocks by half. This event decreases the rate at which new bitcoins are created, leading to reduced supply. Historically, halving has been associated with significant price increases, making it a critical event for investors.',1,'2024-07-22 14:28:53'),
                        (7,'Introduction to Crypto Mining','Crypto mining is the process of validating transactions and adding them to a blockchain. Miners use powerful computers to solve complex mathematical problems, earning new coins as a reward. While mining can be profitable, it requires substantial investment in hardware and electricity.',2,'2024-07-22 14:28:53'),
                        (8,'The Concept of Tokenization','Tokenization involves converting real-world assets, like real estate or art, into digital tokens on a blockchain. This process enables fractional ownership, increased liquidity, and easier transfer of assets. Tokenization is opening new opportunities for investment and asset management.',1,'2024-07-22 14:28:53'),
                        (9,'The Impact of Blockchain on Supply Chain Management','Blockchain technology is transforming supply chain management by providing greater transparency and traceability. With an immutable ledger, companies can track products from origin to delivery, reducing fraud, improving efficiency, and ensuring product authenticity.',3,'2024-07-22 14:28:53'),
                        (10,'How to Choose a Cryptocurrency Exchange','Selecting the right cryptocurrency exchange is crucial for trading success. Key factors to consider include security features, fees, available coins, user interface, and customer support. Researching and comparing different exchanges will help you find a platform that suits your needs and preferences.',4,'2024-07-22 14:28:53'),
                        (14,'Is Ethereum 2.0 the Future of DeFi?','With the recent upgrade to Ethereum 2.0',1,'2024-08-19 08:45:04'),
                        (15,'The Role of Stablecoins in the Crypto Market','Stablecoins have become a major part of the cryptocurrency ecosystem. How crucial do you think they are for the stability and growth of the crypto market?',2,'2024-08-19 08:45:04'),
                        (16,'Bitcoin Halving: What’s Your Price Prediction?','With the next Bitcoin halving around the corner, many are predicting a significant price increase. What are your thoughts? Will we see a new all-time high, or is the halving effect already priced in?',3,'2024-08-19 08:45:04'),
                        (17,'NFTs: Bubble or Long-Term Investment?','NFTs have taken the world by storm, but there’s a lot of debate on whether they’re a bubble waiting to burst or a genuine long-term investment. What’s your take?',4,'2024-08-19 08:45:04'),
                        (18,'Are Meme Coins Here to Stay?','Meme coins like Dogecoin and Shiba Inu have seen explosive growth, but are they just a fad, or do they have staying power in the crypto space?',5,'2024-08-19 08:45:04');
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `username` varchar(32) NOT NULL,
                         `password` varchar(32) NOT NULL,
                         `first_name` varchar(32) NOT NULL,
                         `last_name` varchar(32) NOT NULL,
                         `email` varchar(32) NOT NULL,
                         `admin` tinyint(1) NOT NULL,
                         `moderator` tinyint(1) NOT NULL,
                         `blocked` tinyint(1) NOT NULL,
                         `active` tinyint(1) NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES
                        (1,'Alice','pass1','Alice','Johnson','alice_johnson@testmail.com',1,0,0,1),
                        (2,'Bob','pass2','Bob','Peterson','bob_peterson@testmail.com',0,1,0,1),
                        (3,'Charlie','pass3','Charlie','Harper','charlie_harper@testmail.com',0,1,0,1),
                        (4,'Diana','pass4','Diana','Stevens','diana_stevens@testmail.com',0,0,0,1),
                        (5,'Edward','pass5','Edward','Michals','edward_michals@testmail.com',0,0,0,1),
                        (6,'test','test','test','test','test@test.com',0,0,0,0),
                        (7,'testt','test1','testt','testtttt','testtt@testmail.com',0,0,0,0),
                        (8,'James','pass11','James','Bond','james_bond@email.com',0,0,0,1),
                        (9,'Petkominator','pass12','Petko','Asenov','petkominator@mail.com',0,0,0,1),
                        (10,'CryptoGod','pass22','Jimmy','Trollers','cryptogod@mail.com',0,0,0,1);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;
