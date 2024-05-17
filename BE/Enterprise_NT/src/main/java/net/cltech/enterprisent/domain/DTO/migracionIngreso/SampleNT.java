/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.migracionIngreso;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.tools.mappers.MigrationMapper;

/**
 *
 * @author hpoveda
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SampleNT
{

    private String sampleCode;
    private String sampleName;
    private String container;
    private List<TestNT> test;

    public SampleNT(Sample sample)
    {
        this.sampleCode = sample.getCodesample();
        this.sampleName = sample.getName();
        this.container = sample.getContainer().getName();
        this.test = MigrationMapper.toDtoTestNT(sample.getTests());
    }

}
