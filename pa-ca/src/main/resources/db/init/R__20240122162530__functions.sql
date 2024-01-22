------------------------------------------------------------------------------------------------------------------------
--------------------------------------------- PA'CA PROCEDURES & FUNCTIONS ---------------------------------------------
------------------------------------------------------------------------------------------------------------------------
    
    
-- FUNCTION: paca_generate_random_name
-- DESCRIPTION: Generates Human-like random names
-- RETURNS: VARCHAR with variable size
-- INVERSE: DROP FUNCTION public.paca_generate_random_name()
CREATE OR REPLACE FUNCTION public.paca_generate_random_name()
    RETURNS CHARACTER VARYING
    LANGUAGE plpgsql
AS $function$
DECLARE
    fname text;
    sname text;
BEGIN
    SELECT array_to_string(array(
        SELECT word
        FROM regexp_split_to_table('ximena,kase,poppy,trace,kathryn,nikolas,cali,ryland,sadie,niklaus,thalia,gerald,marilyn,milo,khaleesi,nicolas,margot,jeremias,helena,anderson,laney,mekhi,evangeline,allen,amiyah,lee,waverly,gustavo,chaya,milan,addisyn,miguel,daleyza,dalton,aurelia,emanuel,maliyah,xavier,wynter,kyle,isabelle,fisher,isabela,kyro,blake,larry,delaney,curtis,abigail,zavier,brielle,kenji,raelynn,dario,laurel,omar,liliana,bentlee,kelly,avi,kailani,philip,marilyn,tomas,amira,grady,phoebe,xander,kensley,marcus,khloe,kristian,kallie,anders,addison,baker,miranda,jakari,lucia,felix,kate,skyler,nylah,gabriel,sariyah,rey,promise,jadiel,jianna,emilio,finley,reed,gabriella,eddie,isla,leo,amina,cristian,allison,kristian,kori,jaylen,emely,tripp,landry,james,avayah,bennett,adelina,quINTOn,corinne,izaiah,poppy,daxton,giuliana,dakari,holland,oliver,adelaide,finn,journey,archer,holly,elian,iyla,kannon,addilynn,dominick,catherine,gerald,alma,raylan,marlowe,franklin,harleigh,emanuel,khalani,blaze,celine,wallace,lana,alejandro,marlowe,oakley,belle,rome,kiana,connor,leah,marcellus,alayah,caleb,naya,archer,averie,carmelo,bellamy,jedidiah,paislee,chandler,samira,jack,bella,crosBY,kamiyah,koa,macie,dominic,daniela,brennan,mckinley,karsyn,kyla,bear,marceline,alvaro,mckinley,canaan,faith,braylen,miracle,callan,ava,ryder,maria,leandro,aniya,jake,artemis,shiloh,marisol,isaias,arielle,mohammad,leilani,cesar,amira,andres,isabella,quincy,magdalena,davis,amaia,caspian,frances,ahmed,josephine,santana,ashlynn,calum,raya,joey,juliana,caden,aislinn,taylor,everlee,eduardo,avianna,remi,gianna,enzo,harmony,kabir,belen,melvin,dahlia,felipe,danna,moshe,andrea,bradley,serenity,ellis,claire,juelz,zariyah,jaxton,braelyn,jaxtyn,helena,creed,noor,colin,aaliyah,edward,haylee,brady,carly,ridge,princess,yahir,kimber,princeton,cataleya,onyx,claire,robert,aileen,rowen,diana,ayden,kylee,barrett,vivienne,spencer,addilyn,frederick,alaina,rome,heidi,shane,dylan,devin,zaniyah,stephen,magdalena,tru,tessa,harlan,monroe,callahan,angie,leonel,alexa,callan,ella,ricky,rivka,bridger,ryann,jonah,henley,ariel,addisyn,alex,mazikee,brady,edith,cayson,thea,titan,andi,ivan,hannah,marlon,averi,peter,novah,zayd,christina,reuben,zaylee,porter,robin,nixon,remy,eliel,aviana,vihaan,kamiyah,darius,cataleya,tate,carter,forest,journey,rome,davina,alberto,arya,dorian,adley,leonard,elora,jameson,aurelia,aiden,london,brandon,diana,erick,rosalie,gianni,talia,arturo,macy,salvador,jayleen,damian,elle,crew,haven,flynn,chandler,juelz,lexi,amias,luna,graysen,arya,kace,saoirse,parker,harlow,valentino,maya,axton,adele,augustine,hailey,emery,paisleigh,brooks,ainsley,everett,persephone,finn,marianna,dion,anya,khari,mylah,harry,aisha,hamza,madisyn,matteo,aubree,zyaire,sara,zion,tessa,santos,aliyah,ronin,legacy,brayden,molly,watson,vivienne,marshall,isabella,issac,sierra,luca,emilia,patrick,elle,thaddeus,karla,magnus,ariella,paul,sarai,khari,murphy,cannon,jenesis,robin,daphne,alijah,clementine,finley,analia,patrick,hadlee,darren,bellamy,vincenzo,emelia,brayden,alice,tucker,marleigh,brantley,amanda,zaid,adaline,santos,izabella,bruno,freyja,bryan,capri,john,amanda,ari,lexie,gideon,gabriela,chance,khloe,rohan,kayleigh,jovanni,zendaya,darian,cassidy,camilo,ayla,valentino,wrenley,jakobe,sabrina,clay,jaelynn,king,alma,asa,sariah,mylo,ivory,blaine,anna,callum,justice,kelvin,lillie,nehemiah,harmony,zaiden,leighton,cyrus,lainey,graham,avery,carl,anaya,joshua,renata,ashton,zariyah,nolan,lorelei,messiah,emmalynn,shepherd,elena,joe,mikaela,peter,edith,eddie,kaiya,clark,milani,jadiel,alessandra,ben,athena,cyrus,irene,orlando,skylar,kylen,kailani,jagger,eva,ryder,kendall,peter,jaylah,samson,laila,abdullah,phoenix,langston,london,bruce,lilith,marcellus,reese,graham,carolina,rocco,amaris,jason,artemis,ambrose,delilah,frederick,hannah,zain,alena,kenji,jaylah,raiden,maci,jaxxon,maryam,brooks,tori,samuel,sky,braxton,zaylee,miguel,rosalyn,darian,ariana,apollo,regina,ismael,jaylah,briggs,nala,mathias,reese,maximilian,eliana,joziah,jamie,derrick,katalina,chance,kamila,koda,aubrie,emiliano,ophelia,alden,scarlette,ezekiel,lennon,desmond,natalie,xzavier,aspyn,valentino,myla,ismael,sage,gunner,adele,maximiliano,willa,charlie,lucia,alex,eloise,jagger,maisie,matthias,florence,sebastian,lara,omari,skylar,kian,elina,kody,freya,louie,evelyn,brodie,analia,axton,vivienne,milan,avianna,kellen,sierra,jakob,keilani,bode,adriana,samuel,leyla,kingsley,raelyn,dylan,bethany,julien,skyla,dallas,sarai,harold,elyse,jesse,johanna,alejandro,phoenix,canaan,kinsley,yahir,piper,kylen,mabel,tomas,kennedy,landyn,estella,kevin,ryann,nicolas,scarlet,grady,willa,zain,winnie,mekhi,serenity,vihaan,kinley,guillermo,estella,hakeem,evelynn,hassan,katelyn,kylian,kaitlyn,zaid,lily,tate,amina,mohamed,kendra,devin,katie,andre,colette,canaan,khloe,lionel,kiana,nathanael,kelly,raymond,aila,curtis,emerald,emiliano,paloma,jonas,ellianna,benjamin,zariyah,ernesto,angela,ocean,laylani,titan,elodie,abram,alaya,liam,aisha,timothy,tiffany,francis,alaiya,aldo,madeleine,rhys,nylah,yusuf,margaret,keith,olive,romeo,madeline,cal,gemma,weston,nyomi,camden,sawyer,luciano,daleyza,dominik,ivy,waylon,daleyza,tanner,zuri,koda,addilyn,dennis,kenzie,miles,elyse,orlando,jaylin,reign,sariyah,drake,kassidy,morgan,addison,julian,flora,baylor,erin,eithan,nevaeh,rudy,delilah,nash,braelyn,kyng,elliana,gideon,sunny,jordan,hannah,beau,kiana,trevor,luisa,emmitt,julia,rocky,ashley,idris,mariana,kairo,monroe,forrest,ramona,davian,marina,scott,nora,john,ariana,grant,sylvia,cain,lillie,bode,victoria,drew,rosa,elias,oakley,jesus,jane,juelz,liberty,arturo,willow,legacy,bria,tobias,milana,layton,paityn,bear,emely,nixon,kelly,cash,journey,elias,margaret,prince,sunny,hakeem,dorothy,trenton,irene,dalton,ari,jericho,kelsey,edgar,madelyn,brooks,kynlee,brody,kiara,jaxtyn,katelyn,hugh,kathryn,andy,skyler,judson,dallas,canaan,claire,casen,reese,nolan,phoenix,peter,alina,kamdyn,kaydence,zachary,mackenzie,gatlin,winter,lincoln,lilith,abdullah,nadia,dominic,renata,sullivan,aarya,noah,mckenna,ricardo,wren,duke,ruBY,kyson,jocelyn,lucca,opal,rhys,evelyn,blaise,nyla,forrest,gloria,caiden,ariella,joe,yasmin,brady,nora,harold,makayla,quinn,peyton,keith,lia,johan,jianna,bishop,adrianna,lucian,giovanna,armando,jolie,jadiel,remington,cameron,martha,avi,sara,wilder,emberly,deandre,ryann,dakari,lucille,remy,lilah,forrest,emani,nikolas,valentina,malik,miriam,jakob,ariyah,gerald,alayna,james,hadlee,demetrius,emberly,eliezer,cecelia,brennan,paris,sonny,maya,ari,briella,stanley,avayah,philip,zaylee,lucca,addison,lawrence,ivanna,kylo,rosie,coen,ariah,xander,zariah,trace,genevieve,hakeem,alejandra,magnus,simone,waylon,emani,yahir,emerie,wayne,alexis,will,baylee,emmanuel,river,harrison,nola,jesse,mariam,zakai,nayeli,ronald,jaelynn,enzo,makayla,emir,khloe,eddie,harlee,reign,haven,paxton,elsa,zev,shelBY,anderson,kyla,kenji,sydney,logan,karla,seth,mallory,jakari,oakleigh,moises,alondra,issac,rosalia,james', ',') AS word
        ORDER BY random()
        LIMIT 1
    ), '') INTO fname;

    SELECT array_to_string(array(
        SELECT word
        FROM regexp_split_to_table('frederick,hines,salgado,hogan,melendez,banks,moody,cox,moses,calhoun,burch,bruce,aguilar,stephenson,pierce,guerrero,woodward,tyler,newman,phelps,kent,montgomery,mullins,avila,lester,barry,mathis,boyer,serrano,duffy,ferguson,cabrera,tate,pope,dennis,mullins,alvarez,warner,joseph,reynolds,clements,spears,benjamin,alvarado,walls,schneider,brennan,gonzalez,bender,foster,costa,russell,frost,dillon,austin,barnes,case,english,blevins,wang,goodman,bruce,baxter,welch,daniel,lawson,daniels,webster,pena,hunter,mcINTOsh,knox,weeks,campbell,hartman,stokes,avery,silva,johnston,liu,olsen,cummings,nguyen,frederick,magana,mahoney,mays,benjamin,contreras,arnold,brady,kim,o-neal,baker,scott,miranda,BYrd,richardson,mcINTOsh,mckee,avila,lloyd,ingram,pugh,garcia,hayden,powell,marsh,callahan,galindo,schroeder,hines,park,golden,acevedo,kaur,williams,wong,stone,luna,hunter,hammond,steele,delarosa,flynn,haley,walton,williamson,burch,cochran,jefferson,horne,griffith,rowland,dennis,ashley,fitzpatrick,delacruz,portillo,reeves,cunningham,horne,calderon,tang,cherry,rivers,cox,hall,blackwell,weber,parker,moon,hunter,rivas,wilkerson,o-donnell,rosario,mann,nash,lynn,lee,murphy,mclean,melton,weiss,mccormick,mendoza,contreras,barajas,mercado,parrish,maxwell,rivers,sierra,reilly,mercado,donaldson,murray,frank,solis,stevenson,williams,butler,henderson,york,harding,wade,vo,anthony,pollard,zhang,norris,foley,alvarez,graves,welch,alvarado,miller,poole,daugherty,burnett,lamb,roach,blair,richard,bennett,stokes,acevedo,finley,frye,mckay,ferguson,nunez,spence,decker,bush,beck,love,rosas,lopez,wagner,robertson,hahn,mcmahon,kemp,mcdaniel,lucero,barton,macias,vargas,howell,cooper,bates,cruz,herman,booth,warner,grimes,bryan,tyler,stein,shepard,rojas,cook,payne,david,holland,monroe,skinner,browning,mayer,callahan,o-connor,page,rocha,cruz,richardson,ponce,pratt,valdez,griffin,norton,soto,singh,figueroa,sherman,wise,grant,cherry,lara,ball,person,mccarthy,mosley,barnett,daugherty,nielsen,garrett,grimes,villanueva,stout,chase,shelton,willis,stevenson,anderson,beltran,chung,esquivel,conway,bryant,valenzuela,beil,duffy,mills,travis,holland,guerra,berg,brewer,rubio,hull,tucker,hill,jarvis,lester,hudson,nash,whitehead,randall,villalobos,foley,moss,costa,sweeney,colon,shannon,cobb,gilmore,melton,summers,page,moran,gibbs,gillespie,luna,cherry,richard,arroyo,fisher,bowers,macias,wilkinson,bridges,ward,pope,white,payne,mason,valdez,luna,kelley,parks,neal,mora,anthony,koch,drake,barnes,graves,weber,cortez,dickerson,proctor,herman,haynes,golden,wilson,farley,fisher,bartlett,blackwell,patel,barnett,pittman,stewart,copeland,cuevas,kaur,watson,barry,felix,price,parks,hughes,howe,stone,corona,stuart,barron,choi,solomon,ahmed,sparks,phelps,vang,black,crawford,blake,meyer,woods,garrett,lugo,nichols,solis,harrington,gonzales,black,carey,singh,leon,miller,wall,zamora,gutierrez,flores,peters,graves,palacios,choi,gentry,webb,carlson,zhang,choi,leal,owen,bean,boyle,paul,reese,massey,may,shields,peters,raymond,montes,o-donnell,preston,adkins,gonzales,ramos,weaver,mcclain,sandoval,blake,glenn,rojas,lugo,sutton,tanner,burch,jacobs,wiggins,clark,blake,chang,best,campos,hopkins,lucas,hunter,browning,mcgee,o-connell,hester,corona,barber,everett,ellis,pittman,farley,hickman,erickson,villa,hINTOn,holmes,cochran,ibarra,house,sexton,collier,gould,ortiz,frazier,nicholson,quintana,mata,mann,robertson,salas,higgins,lyons,hail,rice,jackson,esparza,fletcher,turner,orozco,murray,booth,stewart,patton,stephens,dickson,roberson,diaz,stephenson,murillo,hudson,guerra,o-neal,novak,reeves,marquez,mays,miles,bullock,butler,lyons,combs,conway,morales,ashley,wang,keith,castillo,butler,barrett,hudson,beasley,petersen,johnston,duarte,lucas,ponce,payne,colon,figueroa,blackwell,woods,rice,goodman,logan,magana,hamilton,vo,livingston,morgan,wise,hill,shah,bond,costa,beasley,cummings,fischer,lowery,buchanan,price,reilly,harris,mclean,tran,foley,ferguson,trejo,corona,ward,trujillo,stevenson,duran,beasley,curry,pitts,rodgers,woods,waters,evans,johns,hutchinson,patrick,shepherd,lucas,hudson,greer,woodard,robertson,chang,cisneros,hickman,cook,fowler,benson,gomez,alfaro,friedman,pittman,le,duran,perkins,pearson,cuevas,montgomery,duran,hansen,silva,mills,castro,keith,dennis,gill,cordova,jackson,baxter,harrington,morales,roman,compton,coffey,salazar,kerr,rodriguez,harding,shields,copeland,singh,serrano,love,durham,zamora,mcmahon,barajas,morse,may,harris,meyers,galvan,pham,nelson,melendez,meyers,kirBY,espinoza,zhang,levy,keith,vazquez,villa,cunningham,lucas,donaldson,collins,mayer,price,ashley,khan,baxter,bailey,singleton,mcclure,crawford,conway,pierce,walton,daniel,duran,shah,bullock,kent,cooper,gilmore,rosales,hebert,mcclure,estes,caldwell,hardin,hancock,odom,clarke,glenn,wright,moran,miranda,santana,cain,mccarthy,ingram,fuller,lindsey,donaldson,hunter,heath,rivers,beard,english,schneider,blevins,brennan,sanford,robertson,avalos,gallegos,kane,miller,booth,blankenship,franklin,sloan,odom,rubio,brennan,mercado,gregory,smith,sparks,ryan,durham,hammond,orr,marin,fuentes,quinn,cummings,wilkins,cole,whitaker,dunn,mckinney,patel,lim,hart,russell,cherry,dixon,estrada,ramsey,cabrera,booker,edwards,perry,cabrera,ayala,chapman,greer,sherman,allison,lyons,collins,keith,conway,bentley,hodge,frederick,sparks,farrell,ho,campbell,king,levy,todd,escobar,bernal,chavez,small,morgan,watkins,grimes,duffy,medina,campos,rich,brooks,hill,henderson,rivers,bowen,branch,tyler,coleman,mcmillan,guzman,noble,hanson,garrett,villanueva,hubbard,esparza,good,sawyer,mckenzie,robinson,clark,ward,armstrong,casey,sampson,mata,morse,king,velazquez,meza,evans,fuller,hunt,jensen,herman,wyatt,mora,mitchell,jacobson,palacios,gilbert,koch,huynh,terrell,rivers,lloyd,sweeney,english,jensen,luna,evans,cole,molina,rich,estes,flynn,randall,combs,tate,gallegos,mayo,mcbride,salinas,rogers,price,duarte,salazar,pearson,bryan,hancock,macdonald,hogan,aguirre,malone,pugh,galvan,donaldson,cruz,vance,woods,stewart,lucas,hudson,hicks,vu,beltran,stevens,hamilton,pruitt,navarro,flores,figueroa,duarte,mack,mendoza,orozco,velasquez,mayo,johnson,howell,leonard,terry,atkinson,kelly,wu,gilbert,potter,dickerson,quinn,rodriguez,mccarty,santiago,hubbard,rangel,lara,webb,stephenson,alfaro,holland,robinson,levy,chambers,gross,ross,whitaker,park,yoder,benjamin,novak,davenport,meza,johns,casey,ahmed,mays,barker,murphy,leach,blevins,meyer,rosales,burnett,winters,conway,acevedo,espinoza,vaughn,ortega,hubbard,lu,melendez,reed,thornton,burton,mcmahon,zimmerman,burch,larson,garcia,raymond,valentine,burnett,rowland,lozano,barajas,pacheco,hogan,stewart,chang,schultz,beasley,hayden,goodman,foley,potter,campbell,hess,whitaker,adkins,hubbard,avalos,aguirre,daniels,todd,salgado,hunt,estes,ballard,gentry,ayers,perry,lu,mayer,yoder,meadows,curtis,rasmussen,kerr,chen,garza,simmons,pineda,vazquez,boone,correa,cross,shepherd,hINTOn,wagner,chambers,sosa,hunter,o-neal,shannon,hodge,cortez,bishop,phan,horne,osborne,newman,maxwell,costa,wheeler,thomas,choi,manning,morton,avery,mcmillan,patton,conner,wall,macdonald,garcia,mendez', ',') AS word
        ORDER BY random()
        LIMIT 1
    ), '') INTO sname;

    RETURN lower(fname || ' ' || sname);
END;
$function$;


-- FUNCTION: paca_generate_random_email
-- DESCRIPTION: Generates Human-like random email
-- RETURNS: VARCHAR with variable size
-- INVERSE: DROP FUNCTION public.paca_generate_random_email()
CREATE OR REPLACE FUNCTION public.paca_generate_random_email()
    RETURNS CHARACTER VARYING
    LANGUAGE plpgsql
AS $function$
DECLARE
    rdomain text;
BEGIN
    SELECT array_to_string(array(
        SELECT word
        FROM regexp_split_to_table('mail,gmail,yahoo,outlook,hotmail,aol,gmx,zoho,proton,live,yandex,skynet,telnet,me,web', ',') AS word
        ORDER BY random()
        LIMIT 1
    ), '') || '.com' INTO rdomain;

    RETURN lower(replace(paca_generate_random_name(), ' ', '') || (random() * 1000)::int || '@' || rdomain);
END;
$function$;


-- FUNCTION: paca_populate_user
-- DESCRIPTION: Adds random row to user table
-- RETURNS: user table
-- INVERSE: DROP FUNCTION public.paca_populate_user(int4)
CREATE OR REPLACE FUNCTION public.paca_populate_user(num integer)
    RETURNS TABLE(
       id INTEGER,
       role_id INTEGER,
       email VARCHAR(320),
       password VARCHAR(64),
       verified BOOLEAN,
       provider VARCHAR(64),
       provider_id VARCHAR(64))
    LANGUAGE plpgsql
AS $function$
BEGIN
    FOR i IN 1..num LOOP
            INSERT INTO public.user (id, role_id, email, password, verified)
            VALUES (
                (SELECT COALESCE(MAX(u.id), 1000) FROM public.user u) + 1,
                (random() + 1)::int,
                paca_generate_random_email(),
                substr(md5(random()::text), 0, 64),
                random() >= 0.5
            );
    END LOOP;

    RETURN QUERY (
        SELECT * FROM public.user res
        WHERE res.id BETWEEN (SELECT MAX(u.id) FROM public.user u) - num + 1 AND (SELECT MAX(u.id) FROM public.user u)
    );
END;
$function$;