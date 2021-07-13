UPDATE openmrs.serialized_object SET name = 'Patients de la file active au cours  de la période (pharmacie)', description = '', type = 'org.openmrs.module.reporting.dataset.definition.DataSetDefinition', subtype = 'org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition', serialization_class = 'org.openmrs.module.reporting.serializer.ReportingSerializer', serialized_data = '<org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition id="1" uuid="338d665c-4486-471c-a62a-ddb1a1307a10" retired="false">
  <name>Patients de la file active au cours  de la période (pharmacie)</name>
  <description></description>
  <dateCreated id="2">2016-06-17 12:04:35 UTC</dateCreated>
  <changedBy id="3" uuid="f5f84dc3-e86c-11e5-8084-e06995eac916"/>
  <dateChanged id="4">2021-06-27 21:19:16 UTC</dateChanged>
  <parameters id="5">
    <org.openmrs.module.reporting.evaluation.parameter.Parameter id="6">
      <name>endDate</name>
      <label>End Date</label>
      <type>java.util.Date</type>
      <required>true</required>
    </org.openmrs.module.reporting.evaluation.parameter.Parameter>
    <org.openmrs.module.reporting.evaluation.parameter.Parameter id="7">
      <name>location</name>
      <label>Location</label>
      <type>org.openmrs.Location</type>
      <required>true</required>
    </org.openmrs.module.reporting.evaluation.parameter.Parameter>
    <org.openmrs.module.reporting.evaluation.parameter.Parameter id="8">
      <name>nbLate</name>
      <label>nbLate</label>
      <type>java.lang.Integer</type>
      <required>true</required>
    </org.openmrs.module.reporting.evaluation.parameter.Parameter>
  </parameters>
  <id>198</id>
  <sqlQuery>SELECT&#xd;
    Identifiant,&#xd;
    I.DateAdmission,&#xd;
    NULL DateEnrolementSoin,&#xd;
    Sexe,&#xd;
    Naissance,&#xd;
    Age,&#xd;
    V.DateDerniereVisite DateDernierePrescription,&#xd;
    D.DateDerniereRegime,&#xd;
    D.nombreJourTTT NbreJourTraitement,&#xd;
    DernierRegimePrescrit,&#xd;
    RegimeDispensation DernierRegimeDispense,&#xd;
    DateARV DateInitiation,&#xd;
    DateFinTraitement,&#xd;
    StatutCTX&#xd;
&#xd;
FROM&#xd;
    (&#xd;
        SELECT&#xd;
            patient_id, identifier Identifiant, gender Sexe, birthdate Naissance, FLOOR(DATEDIFF(:endDate, birthdate)/365.25) AS Age&#xd;
        FROM&#xd;
            patient_identifier i, person p&#xd;
        WHERE&#xd;
                p.person_id = i.patient_id AND&#xd;
                i.voided = 0 AND preferred = 1&#xd;
    ) pi&#xd;
&#xd;
        INNER JOIN&#xd;
    (&#xd;
        SELECT&#xd;
            DateAdmission, e.patient_id, encounter_id, voided&#xd;
        FROM&#xd;
            (&#xd;
                SELECT MAX(encounter_datetime) DateAdmission, patient_id FROM encounter&#xd;
                WHERE encounter_type = 1 AND voided = 0 AND encounter_datetime &lt;= :endDate AND location_id = :location GROUP BY patient_id) FI,&#xd;
            (SELECT patient_id, encounter_datetime, encounter_id, voided FROM encounter WHERE encounter_type = 1 /*AND voided = 0*/ AND location_id = :location) e&#xd;
        WHERE&#xd;
                FI.DateAdmission = e.encounter_datetime&#xd;
          AND FI.patient_id = e.patient_id&#xd;
        GROUP BY patient_id&#xd;
    ) I ON pi.patient_id = I.patient_id&#xd;
&#xd;
        LEFT JOIN (SELECT IF(value_coded = 1065, &apos;OUI&apos;, &apos;NON&apos;) AntecedantARV, encounter_id FROM obs o WHERE o.concept_id = 164540 AND voided = 0) AntARV&#xd;
                  ON AntARV.encounter_id = I.encounter_id&#xd;
        LEFT JOIN&#xd;
    (&#xd;
        SELECT&#xd;
            encounter_id, E.patient_id, DateDerniereVisite&#xd;
        FROM&#xd;
            encounter E,&#xd;
            (&#xd;
                SELECT&#xd;
                    patient_id, MAX(encounter_datetime) DateDerniereVisite&#xd;
                FROM encounter&#xd;
                WHERE&#xd;
                        encounter_datetime &lt;= :endDate AND encounter_type = 2 AND location_id = :location AND voided = 0 GROUP BY patient_id&#xd;
            ) LE&#xd;
        WHERE E.patient_id = LE.patient_id AND E.encounter_datetime = LE.DateDerniereVisite AND E.encounter_type = 2 AND E.voided = 0&#xd;
    ) V ON pi.patient_id = V.patient_id&#xd;
&#xd;
        LEFT JOIN&#xd;
    (&#xd;
        SELECT&#xd;
            MD.person_id,&#xd;
            DateDerniereRegime,&#xd;
            nombreJourTTT,&#xd;
            RegimeDispensation,&#xd;
            DateRuptureARV DateFinTraitement,&#xd;
            ADDDATE(DateRuptureARV, INTERVAL :nbLate DAY) DateXJoursApresRupture&#xd;
&#xd;
        FROM&#xd;
             (SELECT&#xd;
                  MAX(value_datetime) DateDerniereRegime, person_id&#xd;
              FROM obs&#xd;
              WHERE value_datetime &lt;= :endDate AND concept_id = 165033 AND voided = 0 GROUP BY person_id) MR&#xd;
                 LEFT JOIN&#xd;
            (&#xd;
                SELECT o.person_id, c.name RegimeDispensation, obs_datetime&#xd;
                FROM&#xd;
                    (SELECT person_id, concept_id, value_coded, obs_datetime FROM obs WHERE concept_id = 165033 AND voided = 0) o,&#xd;
                    (SELECT concept_id, name  FROM concept_name WHERE LENGTH(name) &lt;= 15/**/ AND locale_preferred = 1) c&#xd;
                WHERE&#xd;
                        c.concept_id = o.value_coded&#xd;
            ) R ON R.obs_datetime = MR.DateDerniereRegime AND R.person_id = MR.person_id&#xd;
&#xd;
                LEFT JOIN(&#xd;
                SELECT&#xd;
                    MAX(value_datetime) DateDerniereDispensation, person_id&#xd;
                FROM obs&#xd;
                WHERE value_datetime &lt;= :endDate AND concept_id = 165010 AND voided = 0 GROUP BY person_id&#xd;
            ) MD&#xd;
                ON MR.DateDerniereRegime = MD.DateDerniereDispensation AND MR.person_id = MD.person_id&#xd;
                LEFT JOIN&#xd;
            (&#xd;
                SELECT person_id, value_datetime DateRuptureARV, obs_datetime FROM obs WHERE concept_id = 165040 AND voided = 0&#xd;
            ) JD ON JD.obs_datetime = MR.DateDerniereRegime AND JD.person_id = MR.person_id&#xd;
&#xd;
                LEFT JOIN&#xd;
            (&#xd;
                SELECT person_id, value_numeric nombreJourTTT, obs_datetime FROM obs WHERE concept_id = 165011 AND voided = 0&#xd;
            ) J ON J.obs_datetime = MR.DateDerniereRegime AND J.person_id = MR.person_id&#xd;
&#xd;
    ) D ON D.person_id = pi.patient_id&#xd;
&#xd;
        LEFT JOIN&#xd;
    (&#xd;
        SELECT c.name DernierRegimePrescrit, encounter_id FROM obs o&#xd;
                                                                   LEFT JOIN concept_name c&#xd;
                                                                             ON c.concept_id = o.value_coded&#xd;
        WHERE o.concept_id = 162240 AND o.voided = 0&#xd;
    ) R&#xd;
    ON R.encounter_id = V.encounter_id&#xd;
&#xd;
        LEFT JOIN (SELECT value_coded, person_id, encounter_id FROM obs WHERE concept_id = 1255 AND voided = 0) TTT&#xd;
                  ON TTT.encounter_id = V.encounter_id&#xd;
&#xd;
        LEFT JOIN (&#xd;
        SELECT IF(value_coded = 164506, &apos;Sans traitement CTX&apos;,&#xd;
                  IF(value_coded = 164508, &apos;Début ou reprise&apos;,&#xd;
                     IF(value_coded = 164507, &apos;En cours&apos;, NULL))) StatutCTX , encounter_id FROM obs WHERE concept_id IN (164773,164505) AND voided = 0&#xd;
    ) CTX&#xd;
                  ON CTX.encounter_id = V.encounter_id&#xd;
&#xd;
        LEFT JOIN&#xd;
    (&#xd;
        SELECT value_datetime DateARV, person_id, e.encounter_id FROM obs o, encounter e&#xd;
        WHERE e.encounter_id = o.encounter_id AND concept_id IN (164588, 165032, 159599) AND o.voided = 0 AND encounter_type = 1 GROUP BY person_id&#xd;
    ) IARV&#xd;
    ON IARV.person_id = I.patient_id&#xd;
        LEFT JOIN (SELECT person_id FROM obs WHERE concept_id = 1543 AND value_datetime &lt;= :endDate AND voided = 0) Deces ON Deces.person_id = pi.patient_id&#xd;
        LEFT JOIN (SELECT person_id, value_datetime FROM obs WHERE concept_id = 164595 AND voided = 0 AND location_id = :location) Transf&#xd;
                  ON Transf.person_id = pi.patient_id AND (Transf.value_datetime BETWEEN I.DateAdmission AND :endDate)&#xd;
WHERE&#xd;
    Transf.person_id IS NULL AND&#xd;
    Deces.person_id IS NULL AND&#xd;
    (&#xd;
            (&#xd;
                    (TTT.value_coded IS NULL OR TTT.value_coded NOT IN (1260,1107)) AND&#xd;
                    (DateDerniereRegime IS NOT NULL)&#xd;
                    AND (DateXJoursApresRupture &gt;= :endDate)&#xd;
                )&#xd;
            OR&#xd;
            (&#xd;
                    DateDerniereRegime IS NULL AND&#xd;
                    DateAdmission BETWEEN DATE(CONCAT_WS(&apos;-&apos;, YEAR(:endDate), MONTH(:endDate), &apos;01&apos;)) AND :endDate AND&#xd;
                    AntecedantARV = &apos;OUI&apos;&#xd;
                )&#xd;
        )&#xd;
&#xd;
GROUP BY pi.patient_id</sqlQuery>
</org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition>', date_created = '2016-06-17 12:04:35', creator = 1, date_changed = '2021-06-27 21:19:16', changed_by = 1, retired = 0, date_retired = null, retired_by = null, retire_reason = null, uuid = '338d665c-4486-471c-a62a-ddb1a1307a10' WHERE serialized_object_id = 198;