select * from zz_lencheck order by sno;
update zz_lencheck set xpath=trim(xpath) where sno>141;
select * from zz_lencheck where sno>78  order by sno;

select '/MCCI_IN200100UV01'||xpath from zz_lencheck where sno=57 and xpath is not null;
select xpath,replace(xpath,'pri maryRole','primaryRole') from zz_lencheck where sno>77;
update zz_lencheck set element=replace(element,' ','');
update element,replace(element,' ','') from zz_lencheck;
commit;
--replace(xpath,'pri maryRole','primaryRole') where sno>51 and xpath is not null; 

update zz_lencheck set xpath='/MCCI_IN200100UV01'||xpath where sno=78 and xpath is not null;
--commit;

//deleting duplicates
delete from zz_lencheck where rowid not in(
select max(rowid) from zz_lencheck group by sno );


select 'y',section,element,datalen,case,'len',
'{"xpath":[{"field":"'||replace(xpath,'"','\"')||'","value":"'||substr((upper(dbms_random.string('A', 1))||round(dbms_random.value(1, 9))|| dbms_random.string('L', 1)||dbms_random.string('A', 400)),0,datalen)||'"}]}'
xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||datalen||'_'||'p'
from  zz_lencheck where sno>77 and xpath is not null;

--Test data creation for Length check validation
select Flag,section,element,datalen,case,len,xpath ,Filename from (
select 'y' Flag,section,element,datalen,'p' case,'len' len,
'{"xpath":[{"field":"'||replace(xpath,'"','\"')||'","value":"'||substr((upper(dbms_random.string('A', 1))||round(dbms_random.value(1, 9))|| dbms_random.string('L', 1)||dbms_random.string('A', 400)),0,datalen)||'"}]}'
xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||datalen||'_'||'p' Filename
from  zz_lencheck 
union
select 'y' Flag,section,element,datalen-1,'p' case,'len',
'{"xpath":[{"field":"'||replace(xpath,'"','\"')||'","value":"'||substr((upper(dbms_random.string('A', 1))||round(dbms_random.value(1, 9))|| dbms_random.string('L', 1)||dbms_random.string('A', 400)),0,datalen-1)||'"}]}'
xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||(datalen-1)||'_'||'p'
from  zz_lencheck 
union
select 'y',section,element,datalen+1,'n' case,'len',
'{"xpath":[{"field":"'||replace(xpath,'"','\"')||'","value":"'||substr((upper(dbms_random.string('A', 1))||round(dbms_random.value(1, 9))|| dbms_random.string('L', 1)||dbms_random.string('A', 400)),0,datalen+1)||'"}]}'
xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||(datalen+1)||'_'||'n'
from  zz_lencheck  
union
select 'y' Flag,section,element,datalen-datalen+1,'p' case,'len',
'{"xpath":[{"field":"'||replace(xpath,'"','\"')||'","value":"'||substr((upper(dbms_random.string('A', 1))||round(dbms_random.value(1, 9))|| dbms_random.string('L', 1)||dbms_random.string('A', 400)),0,datalen-datalen+1)||'"}]}'
xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||(datalen-datalen+1)||'_'||'p'
from  zz_lencheck
union
select 'y' Flag,section,element,datalen-datalen,'p' case,'len',
'{"xpath":[{"field":"'||replace(xpath,'"','\"')||'","value":"'||substr((upper(dbms_random.string('A', 1))||round(dbms_random.value(1, 9))|| dbms_random.string('L', 1)||dbms_random.string('A', 400)),0,datalen-datalen)||'"}]}'
 xpath,REPLACE(section,'.','')||'_'||substr(element,0,15)||'_'||(datalen-datalen)||'_'||'p'
from  zz_lencheck) 
 dual; --where section in ('B.8.1.1.1','B.8.1.1.2','B.8.2.2.6','B.8.2.2.7','B.8.2.2.8') and datalen=1;
 
 select section from zz_lencheck  where sno=1;
 select replace(xpath,'"','\"') from zz_lencheck where sno=43;