package ru.kosolap.json;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "requestId",
    "partNumber",
    "partCount",
    "hash",
    "maxLength",
    "alphabet"
})
@XmlRootElement(name = "CrackHashManagerRequest", namespace = "http://ccfit.nsu.ru/schema/crack-hash-request")
public class CrackHashManagerRequest {

    @XmlElement(name = "RequestId", namespace = "http://ccfit.nsu.ru/schema/crack-hash-request", required = true)
    protected String requestId;
    @XmlElement(name = "PartNumber", namespace = "http://ccfit.nsu.ru/schema/crack-hash-request")
    protected int partNumber;
    @XmlElement(name = "PartCount", namespace = "http://ccfit.nsu.ru/schema/crack-hash-request")
    protected int partCount;
    @XmlElement(name = "Hash", namespace = "http://ccfit.nsu.ru/schema/crack-hash-request", required = true)
    protected String hash;
    @XmlElement(name = "MaxLength", namespace = "http://ccfit.nsu.ru/schema/crack-hash-request")
    protected int maxLength;
    @XmlElement(name = "Alphabet", namespace = "http://ccfit.nsu.ru/schema/crack-hash-request", required = true)
    protected CrackHashManagerRequest.Alphabet alphabet;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String value) {
        this.requestId = value;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int value) {
        this.partNumber = value;
    }

    public int getPartCount() {
        return partCount;
    }

    public void setPartCount(int value) {
        this.partCount = value;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String value) {
        this.hash = value;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int value) {
        this.maxLength = value;
    }

    public CrackHashManagerRequest.Alphabet getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(CrackHashManagerRequest.Alphabet value) {
        this.alphabet = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "symbols"
    })
    public static class Alphabet {

        @XmlElement(namespace = "http://ccfit.nsu.ru/schema/crack-hash-request")
        protected List<String> symbols;

        public List<String> getSymbols() {
            if (symbols == null) {
                symbols = new ArrayList<>();
            }
            return this.symbols;
        }
    }
}
